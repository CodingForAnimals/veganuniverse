package org.codingforanimals.veganuniverse.create.domain.place

import com.google.firebase.firestore.PropertyName

data class PlaceFormDomainEntity(
    @PropertyName("lat_lng_uid") val latLngUID: String,
    @PropertyName("geo_hash") val geoHash: String,
    @PropertyName("address_components") val addressComponents: PlaceAddressDomainEntity,
    val name: String,
    @PropertyName("opening_hours") val openingHours: String,
    val type: String,
    val tags: List<String>,
)

data class PlaceAddressDomainEntity(
    @PropertyName("street_address") val streetAddress: String,
    val locality: String,
    val province: String,
    val country: String,
)