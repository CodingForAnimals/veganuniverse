@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.places.presentation.home.state

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.places.presentation.home.PlaceViewEntity
import org.codingforanimals.places.presentation.home.PlacesHomeViewModel

class PlacesHomeSavedStateHandler {
    private var savedState: PlacesHomeSavedState? = null

    fun saveState(uiState: PlacesHomeViewModel.UiState) {
        savedState = uiState.toSavedState()
    }

    fun restoreState(): PlacesHomeViewModel.UiState? {
        return savedState?.toUiState()
    }

    private fun PlacesHomeViewModel.UiState.toSavedState(): PlacesHomeSavedState {
        return PlacesHomeSavedState(
            userLocationState = userLocationState,
            latLng = cameraPositionState.position.target,
            zoom = cameraPositionState.position.zoom,
            bearing = cameraPositionState.position.bearing,
            placesState = placesState,
            selectedPlace = selectedPlace,
            isFocused = isFocused,
            filterState = filterState,
            snackbarPreviouslyShown = snackbarPreviouslyShown,
            initialSheetValue = initialSheetValue,
        )
    }

    private fun PlacesHomeSavedState.toUiState(): PlacesHomeViewModel.UiState {
        val position =
            CameraPosition.builder().target(latLng).zoom(zoom).bearing(bearing).build()
        val cameraPositionState = CameraPositionState(position)
        return PlacesHomeViewModel.UiState(
            userLocationState = userLocationState,
            cameraPositionState = cameraPositionState,
            placesState = placesState,
            selectedPlace = selectedPlace,
            isFocused = isFocused,
            filterState = filterState,
            snackbarPreviouslyShown = snackbarPreviouslyShown,
            initialSheetValue = initialSheetValue,
            isRestored = true,
        )
    }

    private data class PlacesHomeSavedState(
        val userLocationState: UserLocationState,
        val latLng: LatLng,
        val zoom: Float,
        val bearing: Float,
        val placesState: PlacesState,
        val selectedPlace: PlaceViewEntity?,
        val isFocused: Boolean,
        val filterState: FilterState,
        val snackbarPreviouslyShown: Boolean,
        val initialSheetValue: SheetValue,
    )
}