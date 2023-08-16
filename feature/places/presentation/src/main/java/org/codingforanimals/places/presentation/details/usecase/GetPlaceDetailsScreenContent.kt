package org.codingforanimals.places.presentation.details.usecase

import org.codingforanimals.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.places.presentation.entity.PlaceViewEntity

internal class GetPlaceDetailsScreenContent {
    operator fun invoke(placeViewEntity: PlaceViewEntity): List<PlaceDetailsScreenItem> {
        return mutableListOf(
            PlaceDetailsScreenItem.Hero(
                imageUrl = placeViewEntity.imageRef,
            ),
            PlaceDetailsScreenItem.Header(
                title = placeViewEntity.name,
                rating = placeViewEntity.rating
            ),
            PlaceDetailsScreenItem.AddressAndOpeningHours(
                addressComponents = placeViewEntity.addressComponents,
                openingHours = placeViewEntity.openingHours
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