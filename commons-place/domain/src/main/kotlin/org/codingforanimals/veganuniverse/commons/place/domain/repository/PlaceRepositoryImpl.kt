package org.codingforanimals.veganuniverse.commons.place.domain.repository

import android.os.Parcelable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.codingforanimals.veganuniverse.commons.place.data.source.PlaceRemoteDataSource
import org.codingforanimals.veganuniverse.commons.place.shared.model.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceCard

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
}