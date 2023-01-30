package org.codingforanimals.places.presentation

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

private const val TAG = "PlacesMapUtils"

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.locationFlow() = callbackFlow {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            try {
                result.lastLocation?.let { trySend(it); close() }
            } catch (e: Throwable) {
                android.util.Log.e(TAG, e.stackTraceToString())
            }
        }
    }
    requestLocationUpdates(
        createLocationRequest(),
        callback,
        android.os.Looper.getMainLooper()
    ).addOnFailureListener {
        close(it)
    }
    awaitClose {
        removeLocationUpdates(callback)
    }
}

private fun createLocationRequest(): LocationRequest {
    return LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
    }
}

internal suspend fun animateToLocation(cameraPositionState: CameraPositionState, location: LatLng) {
    cameraPositionState.animate(
        durationMs = 1_000,
        update = CameraUpdateFactory.newCameraPosition(
            CameraPosition(location, 16f, 0f, 0f)
        ),
    )
}

internal fun LatLng.distanceTo(latLng: LatLng): Double =
    SphericalUtil.computeDistanceBetween(this, latLng)