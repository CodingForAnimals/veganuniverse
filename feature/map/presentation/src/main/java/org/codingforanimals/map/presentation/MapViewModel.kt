package org.codingforanimals.map.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private val argentinaLatLtn = LatLng(-34.603722, -58.381592)

class MapViewModel(
    context: Context
) : ViewModel() {

    private val mapUiStateEmitter = MutableStateFlow(MapUiState())
    val mapUiState: StateFlow<MapUiState> = mapUiStateEmitter

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun fetchUserLocation() {
        viewModelScope.launch {
            fusedLocationClient.locationFlow().collect { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                mapUiStateEmitter.emit(MapUiState(true, latLng))
            }
        }
    }

    data class MapUiState(
        val isUserLocationEnabled: Boolean = false,
        val initialCameraPosition: LatLng = argentinaLatLtn,
    )
}