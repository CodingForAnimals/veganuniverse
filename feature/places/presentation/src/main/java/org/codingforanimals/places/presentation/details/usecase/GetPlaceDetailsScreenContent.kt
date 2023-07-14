package org.codingforanimals.places.presentation.details.usecase

import org.codingforanimals.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.places.presentation.model.PlaceViewEntity

class GetPlaceDetailsScreenContent {
    operator fun invoke(placeViewEntity: PlaceViewEntity): List<PlaceDetailsScreenItem> {
        val fullAddress = "Bme. Mitre 123, Monte Grande, Buenos Aires"
        return mutableListOf(
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
            ),
            PlaceDetailsScreenItem.Reviews,
        )
    }
}