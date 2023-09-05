package org.codingforanimals.veganuniverse.core.location

import android.app.PendingIntent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.codingforanimals.veganuniverse.core.location.model.LocationResponse

interface UserLocationManager {
    val userLocation: StateFlow<LocationResponse>
    fun fetchUserLocation()
    fun requestUserEnableLocationService(): Flow<PendingIntent>
}
