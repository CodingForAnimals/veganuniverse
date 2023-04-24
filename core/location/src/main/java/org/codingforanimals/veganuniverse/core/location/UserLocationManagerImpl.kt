package org.codingforanimals.veganuniverse.core.location

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val TAG = "UserLocationManagerImpl"

internal class UserLocationManagerImpl(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val context: Context,
) : UserLocationManager {

    private val scope = CoroutineScope(coroutineDispatcherProvider.io())

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private var userLocationJob: Job? = null

    private var _userLocation =
        MutableStateFlow<LocationResponse>(LocationResponse.LocationNotRequested)

    override val userLocation: StateFlow<LocationResponse>
        get() = _userLocation

    @SuppressLint("MissingPermission")
    override fun fetchUserLocation() {
        userLocationJob?.cancel()
        userLocationJob = scope.launch {
            Log.e("pepe", "loading")
            _userLocation.value = LocationResponse.LocationLoading
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location -> handleSuccess(location) }
                .addOnFailureListener { exception -> handleException(exception) }

        }
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
            Log.e(TAG, "pepe Location services disabled")
            LocationResponse.LocationServiceDisabled
        } else {
            Log.e(TAG, "pepe Location fetched successfully $location")
            LocationResponse.LocationGranted(location.latitude, location.longitude)
        }
        _userLocation.value = userLocationSuccessResponse
    }

    private fun handleException(exception: Throwable) {
        val userLocationErrorResponse = when (exception) {
            is SecurityException -> {
                Log.e(
                    TAG,
                    "pepe Location permissions not granted. ${exception.stackTraceToString()}"
                )
                LocationResponse.PermissionsNotGranted
            }
            else -> {
                Log.e(TAG, "pepe Failure getting user location")
                LocationResponse.UnknownError
            }
        }
        _userLocation.value = userLocationErrorResponse
    }
}

sealed class LocationResponse {
    object LocationLoading : LocationResponse()
    object LocationNotRequested : LocationResponse()
    object PermissionsNotGranted : LocationResponse()
    object LocationServiceDisabled : LocationResponse()
    object UnknownError : LocationResponse()
    data class LocationGranted(val latitude: Double, val longitude: Double) : LocationResponse()
}


interface UserLocationManager {
    val userLocation: Flow<LocationResponse>
    fun fetchUserLocation()
    fun requestUserEnableLocationService(): Flow<PendingIntent>
}

val userLocationModule = module {
    singleOf(::UserLocationManagerImpl) bind UserLocationManager::class
}
