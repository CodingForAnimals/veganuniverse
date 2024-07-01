package org.codingforanimals.veganuniverse.place.presentation.home

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.place.home.GetPlaceAutocompleteIntent
import org.codingforanimals.veganuniverse.place.home.GetPlaceLocationData
import org.codingforanimals.veganuniverse.place.home.GetPlaces
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceCardSorter
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceTag
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceType
import org.codingforanimals.veganuniverse.place.presentation.home.model.PlaceCardUI
import org.codingforanimals.veganuniverse.place.presentation.home.model.toUI
import org.codingforanimals.veganuniverse.place.presentation.utils.visibleRadiusInKm
import org.codingforanimals.veganuniverse.services.location.UserLocationManager
import org.codingforanimals.veganuniverse.services.location.model.LocationResponse

internal class PlacesHomeViewModel(
    private val userLocationManager: UserLocationManager,
    private val getPlaces: GetPlaces,
    private val getPlaceAutocompleteIntent: GetPlaceAutocompleteIntent,
    private val getPlaceLocation: GetPlaceLocationData,
) : ViewModel() {

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    private val mutableUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = mutableUiState.asStateFlow()

    private val mutableFiltersState: MutableStateFlow<FilterState> = MutableStateFlow(FilterState())
    val filtersState: StateFlow<FilterState> = mutableFiltersState.asStateFlow()

    private val searchPlacesChannel: Channel<SearchPlacesParams> = Channel()
    private val filterPlacesChannel: Channel<FilterState> = Channel()
    val placesState: StateFlow<PlacesState> = channelFlow {
        searchPlacesChannel.receiveAsFlow().collectLatest {
            send(PlacesState.Loading)
            _sideEffects.send(SideEffect.PartiallyExpand)
            val result = when (
                val status = getPlaces(
                    latitude = it.center.latitude,
                    longitude = it.center.longitude,
                    radiusKm = it.radiusKm,
                )
            ) {
                GetPlaces.Result.Error -> PlacesState.Error
                is GetPlaces.Result.Success -> PlacesState.Success(
                    places = status.places.map { card -> card.toUI() },
                    filterState = it.filterState,
                    searchCenter = it.center,
                    searchRadiusInKm = it.radiusKm,
                    zoom = it.zoom,
                )
            }
            send(result)

            filterPlacesChannel.receiveAsFlow().collectLatest { filtersState ->
                mutableFiltersState.value = filtersState
                (result as? PlacesState.Success)?.let { placesState ->
                    send(placesState.copy(filterState = filtersState))
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PlacesState.Loading,
    )

    val userLocationState: StateFlow<UserLocationState> =
        userLocationManager.userLocation.transform {
            emit(
                when (it) {
                    LocationResponse.LocationLoading -> {
                        UserLocationState.Loading
                    }

                    is LocationResponse.LocationServiceDisabled -> {
                        requestUserEnableLocationService()
                        UserLocationState.Unavailable
                    }

                    LocationResponse.LocationNotRequested -> {
                        userLocationManager.requestUserLocation()
                        UserLocationState.Unavailable
                    }

                    LocationResponse.UnknownError -> {
                        _sideEffects.send(SideEffect.ShowSnackbar(Snackbar.LocationError))
                        UserLocationState.Unavailable
                    }

                    LocationResponse.PermissionsNotGranted -> {
                        if (!uiState.value.permissionSnackbarPreviouslyShown) {
                            _sideEffects.send(SideEffect.ShowSnackbar(Snackbar.LocationDenied))
                            mutableUiState.value =
                                uiState.value.copy(permissionSnackbarPreviouslyShown = true)
                        }
                        UserLocationState.Unavailable
                    }

                    is LocationResponse.LocationGranted -> {
                        _sideEffects.send(
                            SideEffect.ZoomInLocation(
                                latLng = LatLng(it.latitude, it.longitude),
                                zoomIn = true,
                                animationSuccessCallback = ::searchPlaces,
                                animationErrorCallback = {},
                            )
                        )
                        UserLocationState.Success
                    }
                }
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = UserLocationState.Loading,
            started = SharingStarted.Lazily,
        )

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
                viewModelScope.launch {
                    userLocationManager.requestUserLocation()
                }
            }

            Action.OnRefreshPlacesButtonClick -> {
                viewModelScope.launch {
                    searchPlaces()
                }
            }

            Action.OnFilterChipClick -> {
                openFilterDialog(FilterDialog.Filter)
            }

            Action.OnSortChipClick -> {
                openFilterDialog(FilterDialog.Sort)
            }

            Action.OnFilterDialogDismissRequest -> {
                mutableFiltersState.value = filtersState.value.copy(visibleDialog = null)
            }

            is Action.OnFilterRequest -> {
                onFilterRequest(action.newPlaceType, action.newActiveTags)
            }

            is Action.OnSortRequest -> {
                onSortRequest(action.newSorter)
            }

            Action.OnMapClick -> {
                mutableUiState.value = uiState.value.copy(isFocused = false)
            }

            is Action.OnPlaceClick -> {
                onPlaceClick(action.place)
            }

            Action.OnOpenSearchCityGoogleMapsOverlay -> {
                openLocationOnlyAutocompleteOverlay()
            }

            is Action.OnLocationFromOverlaySelected -> {
                zoomInAndRefreshPlaces(action.activityResult)
            }
        }
    }

    private fun zoomInAndRefreshPlaces(activityResult: ActivityResult) {
        val intent = activityResult.data
        if (activityResult.resultCode == Activity.RESULT_OK && intent != null) {
            viewModelScope.launch {
                when (val result = getPlaceLocation(intent)) {
                    GetPlaceLocationData.Result.Error -> Unit
                    is GetPlaceLocationData.Result.Success -> {
                        _sideEffects.send(
                            SideEffect.ZoomInLocation(
                                latLng = LatLng(result.latitude, result.longitude),
                                zoomIn = true,
                                animationSuccessCallback = ::searchPlaces,
                            )
                        )
                    }
                }
            }
        }
    }

    fun onEnableLocationResult(resultCode: Int) {
        viewModelScope.launch {
            if (resultCode == Activity.RESULT_OK) {
                userLocationManager.requestUserLocation()
            } else {
                _sideEffects.send(SideEffect.ShowSnackbar(Snackbar.LocationDisabled))
            }
        }
    }

    private fun handleBackClick() {
        if (uiState.value.isFocused) {
            mutableUiState.value = uiState.value.copy(isFocused = false)
        } else {
            viewModelScope.launch {
                _sideEffects.send(SideEffect.NavigateUp)
            }
        }
    }

    private suspend fun searchPlaces() {
        mutableUiState.value = uiState.value.copy(selectedPlace = null)
        searchPlacesChannel.send(
            SearchPlacesParams(
                center = uiState.value.cameraPositionState.position.target,
                radiusKm = uiState.value.cameraPositionState.visibleRadiusInKm(),
                zoom = uiState.value.cameraPositionState.position.zoom,
                filterState = filtersState.value,
            )
        )
    }

    private suspend fun requestUserEnableLocationService() {
        userLocationManager.requestUserEnableLocationService().firstOrNull()?.let {
            val intentSenderRequest = IntentSenderRequest.Builder(it).build()
            _sideEffects.send(SideEffect.LocationServiceEnableRequest(intentSenderRequest))
        }
    }


    private fun onFilterRequest(newPlaceType: PlaceType?, newActiveTags: List<PlaceTag>) {
        val filtersState = filtersState.value.copy(
            activePlaceType = newPlaceType,
            activePlaceTags = newActiveTags,
            visibleDialog = null,
        )

        viewModelScope.launch {
            filterPlacesChannel.send(filtersState)
        }
    }

    private fun onSortRequest(sorter: PlaceCardSorter) {
        val filtersState = filtersState.value.copy(
            sorter = sorter,
            visibleDialog = null,
        )
        viewModelScope.launch {
            filterPlacesChannel.send(filtersState)
        }
    }

    private fun openFilterDialog(dialog: FilterDialog) {
        mutableFiltersState.value = filtersState.value.copy(visibleDialog = dialog)
    }

    private fun onPlaceClick(place: PlaceCardUI) {
        if (uiState.value.isPlaceSelected(place)) {
            viewModelScope.launch {
                _sideEffects.send(SideEffect.NavigateToPlaceDetails(place.geoHash))
            }
        } else {
            focusOnPlace(place)
        }
    }

    private fun focusOnPlace(place: PlaceCardUI) {
        mutableUiState.value = uiState.value.copy(selectedPlace = place, isFocused = true)
        viewModelScope.launch {
            _sideEffects.send(SideEffect.ZoomInLocation(place.markerState.position))
        }
    }

    private fun openLocationOnlyAutocompleteOverlay() {
        viewModelScope.launch {
            _sideEffects.send(
                SideEffect.OpenLocationOnlyAutocompleteOverlay(
                    getPlaceAutocompleteIntent()
                )
            )
        }
    }

    data class UiState(
        val cameraPositionState: CameraPositionState = CameraPositionState(defaultCameraPosition),
        val selectedPlace: PlaceCardUI? = null,
        val isFocused: Boolean = false,
        val permissionSnackbarPreviouslyShown: Boolean = false,
    ) {
        fun isPlaceSelected(entity: PlaceCardUI): Boolean {
            if (!isFocused) return false
            return entity.geoHash == selectedPlace?.geoHash
        }

        companion object {
            private val defaultCameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(-34.0, -64.0),
                5f
            )
        }
    }

    sealed class Action {
        data object OnBackClick : Action()
        data object OnOpenSearchCityGoogleMapsOverlay : Action()
        data object OnExpandSheetButtonClick : Action()
        data object OnSettingsScreenDismissed : Action()
        data object OnRefreshPlacesButtonClick : Action()
        data object OnFilterChipClick : Action()
        data object OnSortChipClick : Action()
        data object OnFilterDialogDismissRequest : Action()
        data class OnFilterRequest(
            val newPlaceType: PlaceType?, val newActiveTags: List<PlaceTag>,
        ) : Action()

        data class OnSortRequest(val newSorter: PlaceCardSorter) : Action()
        data class OnLocationFromOverlaySelected(val activityResult: ActivityResult) : Action()
        data object OnMapClick : Action()
        data class OnPlaceClick(val place: PlaceCardUI) : Action()
    }

    sealed class SideEffect {
        data class NavigateToPlaceDetails(val id: String) : SideEffect()
        data class OpenLocationOnlyAutocompleteOverlay(val intent: Intent) : SideEffect()
        data object NavigateUp : SideEffect()
        data object PartiallyExpand : SideEffect()
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

    sealed class FilterDialog {
        data object Filter : FilterDialog()
        data object Sort : FilterDialog()
    }

    private data class SearchPlacesParams(
        val center: LatLng,
        val radiusKm: Double,
        val zoom: Float,
        val filterState: FilterState,
    )

    sealed class Snackbar(val text: String, val actionLabel: String? = null) {
        data object LocationDenied : Snackbar(
            text = "No tenemos permiso para mostrarte los lugares cercanos a tu ubicación. Podés cambiar esto accediendo a Configuración",
            actionLabel = "Ir a configuración",
        )

        data object LocationError : Snackbar(
            text = "Ha ocurrido un error al intentar obtener tu ubicación",
        )

        data object LocationDisabled : Snackbar(
            text = "No pudimos acceder a tu ubicación",
        )
    }


    internal sealed class PlacesState {
        data object Error : PlacesState()
        data object Loading : PlacesState()
        data class Success(
            val searchCenter: LatLng = LatLng(0.0, 0.0),
            val searchRadiusInKm: Double = 0.0,
            val zoom: Float = 0f,
            private val places: List<PlaceCardUI> = emptyList(),
            private val filterState: FilterState = FilterState(),
        ) : PlacesState() {
            val filteredPlaces: List<PlaceCardUI>
                get() = places.filter { it.isMatchingType && it.containsAllTags }.sortedWith(
                    when (filterState.sorter) {
                        PlaceCardSorter.NAME -> compareBy { it.name.lowercase() }
                        PlaceCardSorter.RATING -> compareByDescending { it.rating }
                    }
                )

            private val PlaceCardUI.isMatchingType: Boolean
                get() = if (filterState.activePlaceType == null) true else {
                    type == filterState.activePlaceType
                }

            private val PlaceCardUI.containsAllTags: Boolean
                get() = tags.containsAll(filterState.activePlaceTags)
        }
    }

    internal data class FilterState(
        val activePlaceType: PlaceType? = null,
        val activePlaceTags: List<PlaceTag> = emptyList(),
        val sorter: PlaceCardSorter = PlaceCardSorter.RATING,
        val visibleDialog: FilterDialog? = null,
    ) {
        val isFilterActive: Boolean
            get() = activePlaceType != null || activePlaceTags.isNotEmpty()
    }

    sealed class UserLocationState {
        data object Loading : UserLocationState()
        data object Success : UserLocationState()
        data object Unavailable : UserLocationState()
    }
}
