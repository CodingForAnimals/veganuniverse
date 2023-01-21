package org.codingforanimals.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.codingforanimals.map.presentation.MapViewModel.MapUiState.Loading
import org.codingforanimals.map.presentation.MapViewModel.MapUiState.Success
import org.codingforanimals.veganuniverse.common.permissions.PermissionsManager
import org.codingforanimals.veganuniverse.common.permissions.VeganUniversePermissions

class MapViewModel(
    private val permissionsManager: PermissionsManager,
) : ViewModel() {

    val uiState = permissionsManager.granted.map {
        Success(it.contains(VeganUniversePermissions.USER_LOCATION))
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )

    sealed interface MapUiState {
        object Loading : MapUiState
        data class Success(val locationPermissionsGranted: Boolean) : MapUiState
    }
}