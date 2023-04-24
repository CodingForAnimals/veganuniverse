package org.codingforanimals.veganuniverse.create.domain.place

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class PlaceFormDomainEntity(
    val id: String,
    val name: String,
    val openingHours: String,
    val type: String,
    val address: String,
    val city: String,
    val tags: List<String>,
    val geoHash: String,
    @ServerTimestamp val date: Date? = null,
    // TODO create keywords
) {
//    @ServerTimestamp
//    val date: Date? = null
}
