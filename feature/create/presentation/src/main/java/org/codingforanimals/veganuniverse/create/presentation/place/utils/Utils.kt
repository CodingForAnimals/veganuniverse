package org.codingforanimals.veganuniverse.create.presentation.place.utils

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng

private const val DEFAULT_GEO_HASH_PRECISION = 22
internal fun LatLng.createGeoHash(): String {
    return GeoFireUtils.getGeoHashForLocation(
        GeoLocation(latitude, longitude),
        DEFAULT_GEO_HASH_PRECISION
    )
}