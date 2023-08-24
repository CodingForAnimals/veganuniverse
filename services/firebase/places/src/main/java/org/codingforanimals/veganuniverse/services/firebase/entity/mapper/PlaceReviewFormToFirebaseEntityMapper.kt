package org.codingforanimals.veganuniverse.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.services.firebase.entity.PlaceReview

internal class PlaceReviewFormToFirebaseEntityMapper :
    OneWayEntityMapper<PlaceReviewForm, PlaceReview> {
    override fun map(obj: PlaceReviewForm): PlaceReview {
        return with(obj) {
            PlaceReview(
                userId = userId,
                username = username,
                rating = rating,
                title = title,
                description = description,
            )
        }
    }
}