package org.codingforanimals.map.presentation

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.codingforanimals.map.presentation.mockdata.Site


class MapViewModel : ViewModel() {

    private val _sideEffect = Channel<SideEffect>()
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun onAction(action: Action) {
        when (action) {
            Action.OnLocationGranted -> {
                val newState = uiState.value.copy(locationGranted = true)
                _uiState.value = newState
            }
            is Action.OnMarkerClicked -> {
                val newState = uiState.value.copy(selectedSite = action.site, showCard = true)
                _uiState.value = newState
            }
            Action.OnCardClose -> {
                val newState = uiState.value.copy(showCard = false)
                _uiState.value = newState
            }
        }
    }

    data class UiState(
        val locationGranted: Boolean = false,
        val selectedSite: Site? = null,
        val showCard: Boolean = false,
    )

    sealed interface Action {
        object OnLocationGranted : Action
        data class OnMarkerClicked(val site: Site) : Action
        object OnCardClose : Action
    }

    sealed interface SideEffect

}