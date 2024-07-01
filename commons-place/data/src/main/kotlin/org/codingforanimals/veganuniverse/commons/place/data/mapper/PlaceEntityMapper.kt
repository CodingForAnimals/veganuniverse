package org.codingforanimals.veganuniverse.commons.place.data.mapper

import com.firebase.geofire.GeoLocation
import org.codingforanimals.veganuniverse.commons.place.data.model.PlaceCardDatabaseEntity
import org.codingforanimals.veganuniverse.commons.place.data.model.PlaceFirestoreEntity
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceCard

internal interface PlaceEntityMapper {
    suspend fun mapPlace(entity: PlaceFirestoreEntity): Place
    suspend fun mapCard(
        geoHash: String,
        location: GeoLocation,
        entity: PlaceCardDatabaseEntity,
    ): PlaceCard
}

