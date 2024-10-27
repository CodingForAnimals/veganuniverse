package org.codingforanimals.veganuniverse.place.data.mapper

import com.firebase.geofire.GeoLocation
import org.codingforanimals.veganuniverse.firebase.storage.model.PublicImageApi
import org.codingforanimals.veganuniverse.place.data.model.PlaceCardDatabaseEntity
import org.codingforanimals.veganuniverse.place.data.model.PlaceFirestoreEntity
import org.codingforanimals.veganuniverse.place.data.source.PlaceFirebaseDataSource
import org.codingforanimals.veganuniverse.place.shared.model.Place
import org.codingforanimals.veganuniverse.place.shared.model.PlaceCard
import org.codingforanimals.veganuniverse.place.shared.model.PlaceTag
import org.codingforanimals.veganuniverse.place.shared.model.PlaceType

internal class PlaceEntityMapperImpl(
    private val publicImageApi: PublicImageApi,
) : PlaceEntityMapper {
    override fun mapPlace(entity: PlaceFirestoreEntity): Place {
        return with(entity) {
            Place(
                geoHash = geoHash,
                name = name,
                addressComponents = addressComponents,
                userId = userId,
                username = username,
                imageUrl = imageId?.let {
                    publicImageApi.getFilePath(
                        path = PlaceFirebaseDataSource.BASE_PLACE_PICTURE_PATH,
                        imageId = it,
                    )
                },
                type = PlaceType.fromString(type),
                description = description,
                rating = rating,
                tags = tags?.mapNotNull { PlaceTag.fromString(it) },
                latitude = latitude ?: 0.0,
                longitude = longitude ?: 0.0,
                createdAt = createdAt?.toDate(),
                openingHours = openingHours.orEmpty(),
            )
        }
    }

    override fun mapCard(
        geoHash: String,
        location: GeoLocation,
        entity: PlaceCardDatabaseEntity,
    ): PlaceCard {
        return PlaceCard(
            geoHash = geoHash,
            name = entity.name,
            rating = entity.rating,
            streetAddress = entity.streetAddress,
            administrativeArea = entity.administrativeArea,
            type = PlaceType.fromString(entity.type),
            imageUrl = entity.imageId?.let {
                publicImageApi.getFilePath(
                    path = PlaceFirebaseDataSource.BASE_PLACE_PICTURE_PATH,
                    imageId = it,
                )
            },
            tags = entity.tags?.mapNotNull { PlaceTag.fromString(it) },
            latitude = location.latitude,
            longitude = location.longitude
        )
    }
}