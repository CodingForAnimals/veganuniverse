package org.codingforanimals.places.presentation.details.usecase

import org.codingforanimals.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.places.presentation.model.PlaceViewEntity
import org.codingforanimals.places.presentation.model.ReviewViewEntity

class GetPlaceDetailsScreenContent {
    operator fun invoke(
        placeViewEntity: PlaceViewEntity,
        userReviewViewEntity: ReviewViewEntity?,
    ): List<PlaceDetailsScreenItem> {
        val fullAddress = "Bme. Mitre 123, Monte Grande, Buenos Aires"
        val items = mutableListOf(
            PlaceDetailsScreenItem.Hero(
                imageUrl = placeViewEntity.imageRef,
            ),
            PlaceDetailsScreenItem.Header(
                title = placeViewEntity.name,
                rating = placeViewEntity.rating
            ),
            PlaceDetailsScreenItem.AddressAndOpeningHours(
                address = fullAddress,
                openingHours = ""
            ),
            PlaceDetailsScreenItem.Description(
                description = placeViewEntity.description,
            ),
            PlaceDetailsScreenItem.Tags(
                tags = placeViewEntity.tags,
            ),
            PlaceDetailsScreenItem.StaticMap(
                marker = placeViewEntity.marker,
                latLng = placeViewEntity.state.position,
            )
        )

//        if (userReviewViewEntity == null) {
//            items.add(PlaceDetailsScreenItem.UserReview)
//        }

        items.add(PlaceDetailsScreenItem.Reviews)

        return items
    }
}