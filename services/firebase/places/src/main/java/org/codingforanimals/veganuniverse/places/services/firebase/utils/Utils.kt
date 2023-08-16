package org.codingforanimals.veganuniverse.places.services.firebase.utils

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation

private const val DEFAULT_GEO_HASH_PRECISION = 22
internal fun createGeoHash(latitude: Double, longitude: Double): String {
    return GeoFireUtils.getGeoHashForLocation(
        GeoLocation(latitude, longitude),
        DEFAULT_GEO_HASH_PRECISION
    )
}