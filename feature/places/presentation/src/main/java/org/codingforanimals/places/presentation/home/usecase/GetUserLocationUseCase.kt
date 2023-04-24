package org.codingforanimals.places.presentation.home.usecase

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.core.ui.permissions.checkIfPermissionGranted

class GetUserLocationUseCase(
    private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val ProducerScope<LocationResponse>.callback: LocationCallback
        get() = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                try {
                    result.lastLocation?.let {
                        trySend(LocationResponse.Granted(LatLng(it.latitude, it.longitude)))
                        close()
                    }
                } catch (e: Throwable) {
                    Log.e("GetUserLocationUseCase", e.stackTraceToString())
                    trySend(LocationResponse.NotGranted)
                }
            }
        }

    private val locationRequest = LocationRequest
        .Builder(Priority.PRIORITY_HIGH_ACCURACY, 20000L)
        .build()

    @SuppressLint("MissingPermission")
    suspend operator fun invoke(): LocationResponse {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val granted = checkIfPermissionGranted(context, permission)
        if (!granted) return LocationResponse.NotGranted
        return callbackFlow {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                callback,
                Looper.getMainLooper(),
            ).addOnFailureListener {
                close(it)
            }
            awaitClose {
                fusedLocationClient.removeLocationUpdates(callback)
            }
        }.catch {
            emit(LocationResponse.NotGranted)
        }.first()
    }

    sealed class LocationResponse {
        object NotGranted : LocationResponse()
        data class Granted(val latLng: LatLng) : LocationResponse()
    }
}