package org.codingforanimals.veganuniverse.services.location

import android.app.PendingIntent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.codingforanimals.veganuniverse.services.location.model.LocationResponse

interface UserLocationManager {
    val userLocation: StateFlow<LocationResponse>
    suspend fun requestUserLocation()
    fun requestUserEnableLocationService(): Flow<PendingIntent>
}
