@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.places.presentation.home

import android.app.Activity
import androidx.activity.result.IntentSenderRequest
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.codingforanimals.places.presentation.entity.PlaceViewEntity
import org.codingforanimals.places.presentation.home.state.FilterState
import org.codingforanimals.places.presentation.home.state.PlacesHomeSavedStateHandler
import org.codingforanimals.places.presentation.home.state.PlacesState
import org.codingforanimals.places.presentation.home.state.UserLocationState
import org.codingforanimals.places.presentation.home.usecase.GetPlacesUseCase
import org.codingforanimals.places.presentation.model.GetPlacesStatus
import org.codingforanimals.places.presentation.utils.visibleRadius
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.core.location.UserLocationManager
import org.codingforanimals.veganuniverse.core.location.model.LocationResponse
import org.codingforanimals.veganuniverse.core.ui.place.PlaceSorter
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType

internal class PlacesHomeViewModel(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val userLocationManager: UserLocationManager,
    private val placesHomeSavedStateHandler: PlacesHomeSavedStateHandler,
    private val getPlacesUseCase: GetPlacesUseCase,
) : ViewModel() {

    private val mainDispatcher = coroutineDispatcherProvider.main()

    private var userLocationJob: Job? = null
    private var getPlacesJob: Job? = null

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    var uiState by mutableStateOf(placesHomeSavedStateHandler.restoreState() ?: UiState())


    init {
        userLocationManager.fetchUserLocation()
        observeUserLocation()
    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> {
                handleBackClick()
            }
            Action.OnExpandSheetButtonClick -> {
                viewModelScope.launch {
                    _sideEffects.send(SideEffect.PartiallyExpand)
                }
            }
            is Action.OnSettingsScreenDismissed -> {
                userLocationManager.fetchUserLocation()
            }
            Action.OnRefreshPlacesButtonClick -> {
                refreshPlaces()
            }
            Action.OnFilterChipClick -> {
                openFilterDialog(FilterDialog.Filter)
            }
            Action.OnSortChipClick -> {
                openFilterDialog(FilterDialog.Sort)
            }
            Action.OnFilterDialogDismissRequest -> {
                val filterState = uiState.filterState.copy(visibleDialog = null)
                uiState = uiState.copy(filterState = filterState)
            }
            is Action.OnFilterRequest -> {
                onFilterRequest(action.newPlaceType, action.newActiveTags)
            }
            is Action.OnSortRequest -> {
                onSortRequest(action.newSorter)
            }
            Action.OnMapClick -> {
                uiState = uiState.copy(isFocused = false)
            }
            is Action.OnPlaceClick -> {
                onPlaceClick(action.place)
            }
        }
    }

    fun onMapLoaded() {
        uiState = uiState.copy(mapLoaded = true)
    }

    fun saveState(initialSheetValue: SheetValue) {
        val state = uiState.copy(initialSheetValue = initialSheetValue)
        placesHomeSavedStateHandler.saveState(state)
    }

    fun onEnableLocationResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            userLocationManager.fetchUserLocation()
        } else {
            viewModelScope.launch {
                _sideEffects.send(SideEffect.ShowSnackbar(Snackbar.LocationDisabled))
            }
        }
    }

    private fun handleBackClick() {
        if (uiState.isFocused) {
            uiState = uiState.copy(isFocused = false)
        } else {
            viewModelScope.launch {
                _sideEffects.send(SideEffect.NavigateUp)
            }
        }
    }

    private fun observeUserLocation() {
        userLocationJob?.cancel()
        userLocationJob = viewModelScope.launch(mainDispatcher.immediate) {
            userLocationManager.userLocation.collectLatest {
                when (it) {
                    LocationResponse.LocationLoading -> {
                        uiState = uiState.copy(userLocationState = UserLocationState.Loading)
                    }
                    is LocationResponse.LocationServiceDisabled -> {
                        uiState = uiState.copy(userLocationState = UserLocationState.Unavailable)
                        requestUserEnableLocationService()
                    }
                    LocationResponse.LocationNotRequested -> {
                        uiState = uiState.copy(userLocationState = UserLocationState.Unavailable)
                        userLocationManager.fetchUserLocation()
                    }
                    LocationResponse.UnknownError -> {
                        uiState = uiState.copy(userLocationState = UserLocationState.Unavailable)
                        _sideEffects.send(SideEffect.ShowSnackbar(Snackbar.LocationError))
                    }
                    LocationResponse.PermissionsNotGranted -> {
                        if (!uiState.snackbarPreviouslyShown) {
                            _sideEffects.send(SideEffect.ShowSnackbar(Snackbar.LocationDenied))
                        }
                        uiState = uiState.copy(
                            userLocationState = UserLocationState.Unavailable,
                            snackbarPreviouslyShown = true
                        )
                    }
                    is LocationResponse.LocationGranted -> {
                        while (!uiState.mapLoaded) {
                            delay(500)
                        }
                        if (!uiState.isRestored) {
                            uiState = uiState.copy(
                                userLocationState = UserLocationState.Success,
                                userMapControlEnabled = false
                            )
                            _sideEffects.send(
                                SideEffect.ZoomInLocation(
                                    latLng = LatLng(it.latitude, it.longitude),
                                    zoomIn = true,
                                    animationSuccessCallback = {
                                        uiState = uiState.copy(userMapControlEnabled = true)
                                        refreshPlaces()
                                    },
                                    animationErrorCallback = {
                                        uiState = uiState.copy(userMapControlEnabled = true)
                                    }
                                )
                            )
                        } else {
                            uiState = uiState.copy(
                                userLocationState = UserLocationState.Success,
                                userMapControlEnabled = true
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun requestUserEnableLocationService() {
        withContext(mainDispatcher) {
            userLocationManager.requestUserEnableLocationService().collectLatest {
                val intentSenderRequest = IntentSenderRequest.Builder(it).build()
                _sideEffects.send(SideEffect.LocationServiceEnableRequest(intentSenderRequest))
            }
        }
    }

    private fun refreshPlaces() {
        getPlacesJob?.cancel()
        getPlacesJob = viewModelScope.launch {
            _sideEffects.send(SideEffect.PartiallyExpand)
            val center = uiState.cameraPositionState.position.target
            val radiusInMeters = uiState.cameraPositionState.visibleRadius()

            getPlacesUseCase(center, radiusInMeters).collectLatest { status ->
                val placesState = when (status) {
                    GetPlacesStatus.Error -> PlacesState.Error
                    GetPlacesStatus.Loading -> PlacesState.Loading
                    is GetPlacesStatus.Success -> PlacesState.Success(
                        rawContent = status.places,
                        filterState = uiState.filterState,
                        searchCenter = center,
                        searchRadiusInMeters = radiusInMeters,
                        zoom = uiState.cameraPositionState.position.zoom,
                    )
                }
                uiState = uiState.copy(placesState = placesState)
            }
        }
    }

    private fun onFilterRequest(newPlaceType: PlaceType?, newActiveTags: List<PlaceTag>) {
        val filterState = uiState.filterState.copy(
            activePlaceType = newPlaceType,
            activePlaceTags = newActiveTags,
            visibleDialog = null,
        )
        val placesState = (uiState.placesState as? PlacesState.Success)
            ?.copy(filterState = filterState)
            ?: return

        uiState = uiState.copy(
            filterState = filterState,
            placesState = placesState,
        )
    }

    private fun onSortRequest(sorter: PlaceSorter) {
        val filterState =
            uiState.filterState.copy(sorter = sorter, visibleDialog = null)
        val placesState =
            (uiState.placesState as? PlacesState.Success)?.copy(filterState = filterState) ?: return
        uiState = uiState.copy(filterState = filterState, placesState = placesState)
    }

    private fun openFilterDialog(dialog: FilterDialog) {
        val filterState = uiState.filterState.copy(visibleDialog = dialog)
        uiState = uiState.copy(filterState = filterState)
    }

    private fun onPlaceClick(place: PlaceViewEntity) {
        if (uiState.isPlaceSelected(place)) {
            viewModelScope.launch {
                _sideEffects.send(SideEffect.NavigateToPlaceDetails(place.id))
            }
        } else {
            focusOnPlace(place)
        }
    }

    private fun focusOnPlace(place: PlaceViewEntity) {
        uiState = uiState.copy(selectedPlace = place, isFocused = true)
        viewModelScope.launch {
            _sideEffects.send(SideEffect.ZoomInLocation(place.state.position))
        }
    }

    data class UiState(
        val userLocationState: UserLocationState = UserLocationState.Loading,
        val cameraPositionState: CameraPositionState = CameraPositionState(defaultCameraPosition),
        val placesState: PlacesState = PlacesState.Success(),
        val selectedPlace: PlaceViewEntity? = null,
        val isFocused: Boolean = false,
        val filterState: FilterState = FilterState(),
        val snackbarPreviouslyShown: Boolean = false,
        val initialSheetValue: SheetValue = SheetValue.Hidden,
        val userMapControlEnabled: Boolean = true,
        val mapLoaded: Boolean = false,
        val isRestored: Boolean = false,
    ) {
        private var _cameraMoved: Boolean = false
        private val PlacesState.Success.cameraMoved: Boolean
            get() = if (_cameraMoved) {
                true
            } else {
                val cameraMoved = cameraPositionState.position.target != searchCenter
                _cameraMoved = cameraMoved
                cameraMoved
            }
        val isRefreshButtonVisible: Boolean
            get() = when (placesState) {
                is PlacesState.Success -> placesState.cameraMoved
                PlacesState.Loading -> false
                PlacesState.Error -> true
            }

        fun isPlaceSelected(entity: PlaceViewEntity): Boolean {
            if (!isFocused) return false
            return entity.id == selectedPlace?.id
        }

        companion object {
            private val defaultCameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(-34.0, -64.0),
                5f
            )
        }
    }

    sealed class FilterDialog {
        object Filter : FilterDialog()
        object Sort : FilterDialog()
    }

    sealed class Action {
        object OnBackClick : Action()
        object OnExpandSheetButtonClick : Action()
        object OnSettingsScreenDismissed : Action()
        object OnRefreshPlacesButtonClick : Action()
        object OnFilterChipClick : Action()
        object OnSortChipClick : Action()
        object OnFilterDialogDismissRequest : Action()
        data class OnFilterRequest(
            val newPlaceType: PlaceType?, val newActiveTags: List<PlaceTag>,
        ) : Action()

        data class OnSortRequest(val newSorter: PlaceSorter) : Action()
        object OnMapClick : Action()
        data class OnPlaceClick(val place: PlaceViewEntity) : Action()
    }

    sealed class SideEffect {
        data class NavigateToPlaceDetails(val id: String) : SideEffect()
        object NavigateUp : SideEffect()
        object PartiallyExpand : SideEffect()
        data class LocationServiceEnableRequest(val intent: IntentSenderRequest) : SideEffect()
        data class ShowSnackbar(val snackbar: Snackbar) : SideEffect()
        data class ZoomInLocation(
            private val latLng: LatLng,
            private val zoomIn: Boolean = false,
            val animationSuccessCallback: (suspend () -> Unit)? = null,
            val animationErrorCallback: (() -> Unit)? = null,
        ) : SideEffect() {
            val cameraUpdate = if (zoomIn) {
                CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ANIMATION_ZOOM)
            } else {
                CameraUpdateFactory.newLatLng(latLng)
            }
            val duration = DEFAULT_ANIMATION_DURATION

            companion object {
                private const val DEFAULT_ANIMATION_ZOOM = 15f
                private const val DEFAULT_ANIMATION_DURATION = 750
            }
        }
    }

    sealed class Snackbar(val text: String, val actionLabel: String? = null) {
        object LocationDenied : Snackbar(
            text = "No tenemos permiso para mostrarte los lugares cercanos a tu ubicación. Podés cambiar esto accediendo a Configuración",
            actionLabel = "Ir a configuración",
        )

        object LocationError : Snackbar(
            text = "Ha ocurrido un error al intentar obtener tu ubicación",
        )

        object LocationDisabled : Snackbar(
            text = "No pudimos acceder a tu ubicación",
        )
    }
}