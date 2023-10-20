package org.codingforanimals.veganuniverse.places.services.entity.mapper

import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.services.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReview as PlaceReviewDomainEntity

internal class PlaceReviewToDomainEntityMapper :
    OneWayEntityMapper<PlaceReview, PlaceReviewDomainEntity> {
    override fun map(obj: PlaceReview): PlaceReviewDomainEntity {
        return with(obj) {
            PlaceReviewDomainEntity(
                id = id,
                userId = userId,
                username = username,
                rating = rating,
                title = title,
                description = description,
                timestampSeconds = timestamp?.seconds ?: 0
            )
        }
    }
}