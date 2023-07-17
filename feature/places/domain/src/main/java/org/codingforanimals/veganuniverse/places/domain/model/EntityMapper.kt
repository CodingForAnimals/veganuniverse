package org.codingforanimals.veganuniverse.places.domain.model

import org.codingforanimals.veganuniverse.services.firebase.api.places.model.PlaceFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.api.places.model.ReviewFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.api.places.model.dto.ReviewDTO
import org.codingforanimals.veganuniverse.services.firebase.api.places.model.dto.ReviewFormDTO

fun PlaceFirebaseEntity.toDomainEntity(): PlaceDomainEntity? {
    return PlaceDomainEntity(
        id = id,
        imageRef = imageRef,
        type = type,
        name = name,
        rating = rating,
        description = description,
        reviewCount = reviewsCount,
        address = address,
        city = city,
        tags = tags,
        latitude = latitude,
        longitude = longitude,
        geoHash = geoHash,
        timestamp = timestampSeconds ?: return null,
        openingHours = "Lu a Vie 9 a 23hs"
    )
}

fun ReviewFormDomainEntity.toDto() =
    ReviewFormDTO(
        userId = userId,
        username = username,
        rating = rating,
        title = title,
        description = description
    )

fun ReviewFirebaseEntity.toDomainEntity(): ReviewDomainEntity? {
    return ReviewDomainEntity(
        userId = userId.ifBlank { return null },
        username = username.ifBlank { return null },
        rating = rating,
        title = title.ifBlank { return null },
        description = description,
        timestamp = timestampSeconds ?: return null
    )
}

internal fun PlaceLocationQueryParams.toFirebaseQueryParams(): org.codingforanimals.veganuniverse.services.firebase.api.places.model.PlaceLocationQueryParams {
    return org.codingforanimals.veganuniverse.services.firebase.api.places.model.PlaceLocationQueryParams(
        latitude, longitude, radiusInMeters
    )
}

internal fun ReviewDTO.toDomainEntity(): ReviewDomainEntity {
    return ReviewDomainEntity(
        userId = userId,
        username = username,
        rating = rating,
        title = title,
        description = description,
        timestamp = timestampSeconds,
    )
}