package org.codingforanimals.veganuniverse.places.presentation.details.entity

import org.codingforanimals.veganuniverse.places.entity.PlaceReview as PlaceReviewDomainEntity

internal fun PlaceReview.toDomainEntity(): PlaceReviewDomainEntity {
    return PlaceReviewDomainEntity(
        id = id,
        userId = userId,
        username = username,
        rating = rating,
        title = title,
        description = description,
        timestampSeconds = 0,
    )
}