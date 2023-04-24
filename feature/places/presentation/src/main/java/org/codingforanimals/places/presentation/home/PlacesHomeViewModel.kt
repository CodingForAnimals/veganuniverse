@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.places.presentation.home

import android.app.Activity
import android.location.Location
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
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import kotlin.math.pow
import kotlin.math.sqrt
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.codingforanimals.places.presentation.home.state.PlacesHomeSavedStateHandler
import org.codingforanimals.places.presentation.home.usecase.GetPlacesUseCase
import org.codingforanimals.places.presentation.utils.distanceTo
import org.codingforanimals.veganuniverse.core.location.LocationResponse
import org.codingforanimals.veganuniverse.core.location.UserLocationManager
import org.codingforanimals.veganuniverse.core.ui.place.PlaceSorter
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider

class PlacesHomeViewModel(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val userLocationManager: UserLocationManager,
    private val placesHomeSavedStateHandler: PlacesHomeSavedStateHandler,
    private val getPlacesUseCase: GetPlacesUseCase,
) : ViewModel() {

    private val ioDispatcher = coroutineDispatcherProvider.io()
    private val mainDispatcher = coroutineDispatcherProvider.main()

    private var userLocationJob: Job? = null
    private var getPlacesJob: Job? = null

    private val userLocation = userLocationManager.userLocation

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    var uiState by mutableStateOf(placesHomeSavedStateHandler.restoreState() ?: UiState())


    init {
        userLocationManager.fetchUserLocation()
        observeUserLocation()
    }

    fun onAction(action: Action) {
        when (action) {
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
                onPlaceCardClick(action.place)
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

    private fun observeUserLocation() {
        userLocationJob?.cancel()
        userLocationJob = viewModelScope.launch(mainDispatcher.immediate) {
            userLocation.collectLatest {
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
            uiState = uiState.copy(placesState = PlacesState.Loading)
            _sideEffects.send(SideEffect.PartiallyExpand)
            val center = uiState.cameraPositionState.position.target
            val radiusInMeters = uiState.cameraPositionState.visibleRadius()
            val placesState = withContext(ioDispatcher) {
                val result = getPlacesUseCase(center, radiusInMeters).getOrNull()
                if (result != null) {
                    val filter = uiState.filterState
                    PlacesState.Success(
                        rawContent = result,
                        filterState = filter,
                        searchCenter = center,
                        searchRadiusInMeters = radiusInMeters,
                        zoom = uiState.cameraPositionState.position.zoom,
                    )
                } else {
                    PlacesState.Error
                }
            }
            uiState = uiState.copy(placesState = placesState)
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

    private fun onPlaceCardClick(place: PlaceViewEntity) {
        if (uiState.isPlaceSelected(place)) {
            // navigate to details screen
            viewModelScope.launch {
                _sideEffects.send(SideEffect.NavigateToPlaceDetails)
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

    private fun CameraPositionState.visibleRadius(): Double {
        this.projection?.visibleRegion?.let { visibleRegion ->
            val distanceWidth = FloatArray(1)
            val distanceHeight = FloatArray(1)
            val farRight = visibleRegion.farRight
            val farLeft = visibleRegion.farLeft
            val nearRight = visibleRegion.nearRight
            val nearLeft = visibleRegion.nearLeft

            Location.distanceBetween(
                (farLeft.latitude + nearLeft.latitude) / 2,
                farLeft.longitude,
                (farRight.latitude + nearRight.latitude) / 2,
                farRight.longitude,
                distanceWidth
            )
            Location.distanceBetween(
                farRight.latitude,
                (farRight.longitude + farLeft.longitude) / 2,
                nearRight.latitude,
                (nearRight.longitude + nearLeft.longitude) / 2,
                distanceHeight
            )
            return sqrt(
                distanceWidth[0].toDouble().pow(2.0) + distanceHeight[0].toDouble().pow(2.0)
            ) / 2
        }
        return 0.0
    }

    data class UiState(
        val userLocationState: UserLocationState = UserLocationState.Loading,
        val cameraPositionState: CameraPositionState = CameraPositionState(),
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
        val canRefreshPlaces: Boolean
            get() = when (placesState) {
                PlacesState.Loading -> false
                PlacesState.Error -> true
                is PlacesState.Success -> {
                    cameraIsNotWithinSearchRadius
                        && cameraIsNotInDefaultPosition
                        && cameraWasNotAnimatedByDeveloper
                        && cameraZoomIsNotBiggerThanLimit
                }
            }

        private val cameraIsNotWithinSearchRadius: Boolean
            get() = with(placesState as PlacesState.Success) {
                cameraPositionState.position.target.distanceTo(searchCenter) > searchRadiusInMeters
                    || cameraPositionState.position.zoom != zoom
            }

        private val cameraIsNotInDefaultPosition: Boolean
            get() = cameraPositionState.position.target != LatLng(0.0, 0.0)

        private val cameraWasNotAnimatedByDeveloper: Boolean
            get() = cameraPositionState.cameraMoveStartedReason != CameraMoveStartedReason.DEVELOPER_ANIMATION

        private val cameraZoomIsNotBiggerThanLimit: Boolean
            get() = cameraPositionState.position.zoom >= 12f

        fun isPlaceSelected(entity: PlaceViewEntity): Boolean {
            if (!isFocused) return false
            return entity.id == selectedPlace?.id
        }
    }

    sealed class PlacesState {
        object Error : PlacesState()
        object Loading : PlacesState()
        data class Success(
            val position: CameraPosition = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 0f),
            val searchCenter: LatLng = LatLng(0.0, 0.0),
            val searchRadiusInMeters: Double = 0.0,
            val zoom: Float = 0f,
            private val rawContent: List<PlaceViewEntity> = emptyList(),
            private val filterState: FilterState = FilterState(),
        ) : PlacesState() {
            val content: List<PlaceViewEntity> = rawContent
                .filter { it.isMatchingType && it.containsAllTags }
                .sortedWith(getSorter)

            private val getSorter
                get() =
                    when (filterState.sorter) {
                        PlaceSorter.NAME -> Comparator<PlaceViewEntity> { t, t2 ->
                            compareValues(
                                t.name,
                                t2.name
                            )
                        }
                        PlaceSorter.RATING -> Comparator { t, t2 ->
                            compareValues(
                                t.rating,
                                t2.rating
                            )
                        }
                        PlaceSorter.REVIEWS -> Comparator { t, t2 ->
                            compareValues(
                                t.rating,
                                t2.rating
                            )
                        }
                        PlaceSorter.DATE -> Comparator { t, t2 ->
                            compareValues(
                                t.rating,
                                t2.rating
                            )
                        }
                    }


            private val PlaceViewEntity.isMatchingType: Boolean
                get() = if (filterState.activePlaceType == null) true else {
                    type == filterState.activePlaceType
                }

            private val PlaceViewEntity.containsAllTags: Boolean
                get() = tags.containsAll(filterState.activePlaceTags)
        }
    }

    data class FilterState(
        val activePlaceType: PlaceType? = null,
        val activePlaceTags: List<PlaceTag> = emptyList(),
        val sorter: PlaceSorter = PlaceSorter.RATING,
        val visibleDialog: FilterDialog? = null,
    ) {
        val isFilterActive: Boolean
            get() = activePlaceType != null || activePlaceTags.isNotEmpty()
    }

    sealed class UserLocationState {
        object Loading : UserLocationState()
        object Success : UserLocationState()
        object Unavailable : UserLocationState()
    }


    sealed class FilterDialog {
        object Filter : FilterDialog()
        object Sort : FilterDialog()
    }

    sealed class Action {
        object OnExpandSheetButtonClick : Action()
        object OnSettingsScreenDismissed : Action()
        object OnRefreshPlacesButtonClick : Action()
        object OnFilterChipClick : Action()
        object OnSortChipClick : Action()
        object OnFilterDialogDismissRequest : Action()
        data class OnFilterRequest(
            val newPlaceType: PlaceType?, val newActiveTags: List<PlaceTag>
        ) : Action()

        data class OnSortRequest(val newSorter: PlaceSorter) : Action()
        object OnMapClick : Action()
        data class OnPlaceClick(val place: PlaceViewEntity) : Action()
    }

    sealed class SideEffect {
        object NavigateToPlaceDetails : SideEffect()
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