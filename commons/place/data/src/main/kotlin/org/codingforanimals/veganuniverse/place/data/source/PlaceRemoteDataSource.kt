package org.codingforanimals.veganuniverse.place.data.source

import android.os.Parcelable
import org.codingforanimals.veganuniverse.place.model.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.place.model.Place
import org.codingforanimals.veganuniverse.place.model.PlaceCard

interface PlaceRemoteDataSource {
    suspend fun getById(id: String): Place?
    suspend fun getByLatLng(latitude: Double, longitude: Double): Place?
    suspend fun queryCardsByGeoLocation(params: GeoLocationQueryParams): List<PlaceCard>
    suspend fun insertPlace(place: Place, imageModel: Parcelable): String
    suspend fun reportPlace(placeId: String, userId: String)
    suspend fun deleteById(id: String)
}

