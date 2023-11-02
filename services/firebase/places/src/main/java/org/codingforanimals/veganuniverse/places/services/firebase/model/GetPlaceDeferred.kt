package org.codingforanimals.veganuniverse.places.services.firebase.model

import com.firebase.geofire.GeoLocation
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.Deferred

data class GetPlaceDeferred(
    val geoLocation: GeoLocation,
    val deferredDataSnapshot: Deferred<DataSnapshot>,
) {
    suspend fun await(): GetPlaceResult {
        return GetPlaceResult(geoLocation, deferredDataSnapshot.await())
    }
}