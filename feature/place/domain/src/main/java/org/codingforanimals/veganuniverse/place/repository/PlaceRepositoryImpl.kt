package org.codingforanimals.veganuniverse.place.repository

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.place.data.source.PlaceRemoteDataSource
import org.codingforanimals.veganuniverse.place.shared.model.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.place.shared.model.Place
import org.codingforanimals.veganuniverse.place.shared.model.PlaceCard
import org.codingforanimals.veganuniverse.place.shared.query.PlaceQueryParams

internal class PlaceRepositoryImpl(
    private val remoteDataSource: PlaceRemoteDataSource,
) : PlaceRepository {
    override suspend fun getById(id: String): Place? {
        return remoteDataSource.getById(id)
    }

    override suspend fun getByIdList(ids: List<String>): List<Place> {
        return coroutineScope {
            ids.map { async { remoteDataSource.getById(it) } }.awaitAll().filterNotNull()
        }
    }

    override fun queryPlacesPagingDataByIds(ids: List<String>): Flow<PagingData<Place>> {
        return remoteDataSource.queryPlacesPagingDataByIds(ids)
    }

    override fun queryPlacesPagingDataFlow(params: PlaceQueryParams): Flow<PagingData<Place>> {
        return remoteDataSource.queryPlacesPagingDataFlow(params)
    }

    override suspend fun getByLatLng(latitude: Double, longitude: Double): Place? {
        return remoteDataSource.getByLatLng(latitude, longitude)
    }

    override suspend fun queryCardsByGeoLocation(params: GeoLocationQueryParams): List<PlaceCard> {
        return remoteDataSource.queryCardsByGeoLocation(params)
    }

    override suspend fun insertPlace(place: Place, imageModel: Parcelable): String {
        return remoteDataSource.insertPlace(place, imageModel)
    }

    override suspend fun reportPlace(placeId: String, userId: String) {
        remoteDataSource.reportPlace(placeId, userId)
    }

    override suspend fun editPlace(placeId: String, userId: String, suggestion: String) {
        remoteDataSource.editPlace(placeId, userId, suggestion)
    }

    override suspend fun deleteById(id: String) {
        return remoteDataSource.deleteById(id)
    }

    override suspend fun validatePlace(id: String) {
        return remoteDataSource.validatePlace(id)
    }
}