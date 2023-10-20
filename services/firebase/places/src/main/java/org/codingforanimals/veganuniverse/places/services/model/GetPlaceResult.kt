package org.codingforanimals.veganuniverse.places.services.model

import com.firebase.geofire.GeoLocation
import com.google.firebase.database.DataSnapshot

data class GetPlaceResult(
    val geoLocation: GeoLocation,
    val dataSnapshot: DataSnapshot,
)