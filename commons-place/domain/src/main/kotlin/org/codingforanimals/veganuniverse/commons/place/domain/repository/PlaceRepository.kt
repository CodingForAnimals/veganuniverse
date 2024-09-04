package org.codingforanimals.veganuniverse.commons.place.domain.repository

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.place.shared.model.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceCard
import org.codingforanimals.veganuniverse.commons.place.shared.query.PlaceQueryParams

interface PlaceRepository {
    suspend fun getById(id: String): Place?
    fun queryPlacesPagingDataByIds(ids: List<String>): Flow<PagingData<Place>>
    fun queryPlacesPagingDataFlow(params: PlaceQueryParams): Flow<PagingData<Place>>
    suspend fun getByIdList(ids: List<String>): List<Place>
    suspend fun getByLatLng(latitude: Double, longitude: Double): Place?
    suspend fun queryCardsByGeoLocation(params: GeoLocationQueryParams): List<PlaceCard>
    suspend fun insertPlace(place: Place, imageModel: Parcelable): String
    suspend fun reportPlace(placeId: String, userId: String)
    suspend fun editPlace(placeId: String, userId: String, suggestion: String)
    suspend fun deleteById(id: String)
    suspend fun validatePlace(id: String)
}

