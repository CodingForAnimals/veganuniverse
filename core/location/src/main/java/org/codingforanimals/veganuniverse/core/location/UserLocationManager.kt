package org.codingforanimals.veganuniverse.core.location

import android.app.PendingIntent
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.core.location.model.LocationResponse

interface UserLocationManager {
    val userLocation: Flow<LocationResponse>
    fun fetchUserLocation()
    fun requestUserEnableLocationService(): Flow<PendingIntent>
}
