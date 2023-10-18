package org.codingforanimals.veganuniverse.services.firebase.utils

import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.PlaceImageType

internal fun createGeoHash(latitude: Double, longitude: Double): String {
    return GeoFireUtils.getGeoHashForLocation(GeoLocation(latitude, longitude))
}

internal fun DatabaseReference.geoQuery(
    params: GeoLocationQueryParams,
    onKeyFound: (String, GeoLocation) -> Unit,
    onGeoQueryDone: () -> Unit,
    onGeoQueryError: (Exception?) -> Unit,
) {
    GeoFire(this).queryAtLocation(
        /* center = */ params.geoLocation,
        /* radius = */ params.radiusKm
    ).addGeoQueryEventListener(
        object : GeoQueryEventListener {
            override fun onKeyExited(key: String?) = Unit
            override fun onKeyMoved(key: String?, location: GeoLocation?) = Unit
            override fun onGeoQueryError(error: DatabaseError?) {
                onGeoQueryError(error?.toException())
            }

            override fun onGeoQueryReady() = onGeoQueryDone()
            override fun onKeyEntered(key: String?, location: GeoLocation?) {
                if (key != null && location != null) {
                    onKeyFound(key, location)
                }
            }
        }
    )
}

internal val GeoLocationQueryParams.geoLocation: GeoLocation
    get() = GeoLocation(latitude, longitude)

internal val PlaceImageType.extension: String
    get() {
        return when (this) {
            PlaceImageType.Picture -> "_1000x1000"
            PlaceImageType.Thumbnail -> "_400x400"
        }
    }