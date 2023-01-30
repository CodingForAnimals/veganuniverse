package org.codingforanimals.places.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.common.dispatcher.CoroutineDispatcherProvider

class PlacesViewModel(
    context: Context,
    private val dispatchers: CoroutineDispatcherProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _sideEffect: Channel<SideEffect> = Channel()
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun onAction(action: UserAction) {
        when (action) {
            UserAction.OnLocationGranted -> {
                _uiState.value = uiState.value.copy(locationGranted = true)
                fetchUserLocation()
            }
        }
    }

    private fun fetchUserLocation() {
        viewModelScope.launch(dispatchers.io()) {
            fusedLocationClient.locationFlow().collect { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                withContext(dispatchers.main()) {
                    _sideEffect.send(SideEffect.ZoomOnUserLocation(latLng))
                }
            }
        }
    }

    data class UiState(
        val locationGranted: Boolean = false
    )

    sealed class UserAction {
        object OnLocationGranted : UserAction()
    }

    sealed class SideEffect {
        data class ZoomOnUserLocation(val latLng: LatLng) : SideEffect()
    }
}