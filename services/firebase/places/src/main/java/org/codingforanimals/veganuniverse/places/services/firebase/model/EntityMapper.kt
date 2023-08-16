package org.codingforanimals.veganuniverse.places.services.firebase.model

import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.places.services.firebase.utils.createGeoHash

internal fun PlaceFirebaseEntity.toDomainEntity(): Place {
    return Place(
        id = id,
        name = name,
        addressComponents = addressComponents,
        imageRef = imageRef,
        type = type,
        description = description,
        rating = rating,
        reviewCount = reviewsCount,
        tags = tags,
        latitude = latitude,
        longitude = longitude,
        geoHash = geoHash,
        timestamp = timestampSeconds,
        openingHours = openingHours
    )
}

internal fun PlaceForm.toFirebaseEntity(): PlaceFormFirebaseEntity {
    return PlaceFormFirebaseEntity(
        name = name,
        addressComponents = addressComponents,
        openingHours = openingHours,
        type = type,
        latitude = latitude,
        longitude = longitude,
        tags = tags,
        geoHash = createGeoHash(latitude, longitude)
    )
}

internal fun PlaceReviewFirebaseEntity.toDomainEntity(): PlaceReview {
    return PlaceReview(
        id = id,
        userId = userId,
        username = username,
        rating = rating,
        title = title,
        description = description,
        timestampSeconds = timestampSeconds,
    )
}

internal fun PlaceReviewForm.toFirebaseEntity(): PlaceReviewFormFirebaseEntity {
    return PlaceReviewFormFirebaseEntity(
        userId = userId,
        username = username,
        rating = rating,
        title = title,
        description = description,
    )
}