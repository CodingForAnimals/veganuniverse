package org.codingforanimals.veganuniverse.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import org.codingforanimals.veganuniverse.core.location.model.LocationResponse

private const val TAG = "UserLocationManagerImpl"

internal class UserLocationManagerImpl(
    private val context: Context,
) : UserLocationManager {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private var _userLocation =
        MutableStateFlow<LocationResponse>(LocationResponse.LocationNotRequested)

    override val userLocation: StateFlow<LocationResponse>
        get() = _userLocation

    @SuppressLint("MissingPermission")
    override suspend fun requestUserLocation() {
        _userLocation.value = LocationResponse.LocationLoading
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location -> handleSuccess(location) }
            .addOnFailureListener { exception -> handleException(exception) }
    }

    override fun requestUserEnableLocationService() = callbackFlow {
        val locReq = LocationRequest.Builder(20000).setMaxUpdates(1).build()
        val request = LocationSettingsRequest.Builder().addLocationRequest(locReq).build()
        val client = LocationServices.getSettingsClient(context)
        client.checkLocationSettings(request).addOnFailureListener {
            when (it) {
                is ResolvableApiException -> trySend(it.resolution)
            }
        }
        awaitClose()
    }

    private fun handleSuccess(location: Location?) {
        val userLocationSuccessResponse = if (location == null) {
            LocationResponse.LocationServiceDisabled
        } else {
            LocationResponse.LocationGranted(location.latitude, location.longitude)
        }
        _userLocation.value = userLocationSuccessResponse
    }

    private fun handleException(exception: Throwable) {
        Log.e(TAG, exception.stackTraceToString())
        val userLocationErrorResponse = when (exception) {
            is SecurityException -> {
                LocationResponse.PermissionsNotGranted
            }

            else -> {
                LocationResponse.UnknownError
            }
        }
        _userLocation.value = userLocationErrorResponse
    }
}
