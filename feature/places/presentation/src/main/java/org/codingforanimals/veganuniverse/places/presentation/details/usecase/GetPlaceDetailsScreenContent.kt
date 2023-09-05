package org.codingforanimals.veganuniverse.places.presentation.details.usecase

import org.codingforanimals.veganuniverse.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.veganuniverse.places.presentation.entity.Place

internal class GetPlaceDetailsScreenContent {
    operator fun invoke(place: Place): List<PlaceDetailsScreenItem> {
        val content = mutableListOf(
            PlaceDetailsScreenItem.Hero(
                url = place.imageRef,
            ),
            PlaceDetailsScreenItem.Header(
                title = place.name,
                rating = place.rating
            ),
            PlaceDetailsScreenItem.AddressAndOpeningHours(
                addressComponents = place.addressComponents,
                openingHours = place.openingHours
            ),
            PlaceDetailsScreenItem.Tags(
                tags = place.tags,
            ),
            PlaceDetailsScreenItem.StaticMap(
                marker = place.marker,
                latLng = place.state.position,
            ),
            PlaceDetailsScreenItem.Reviews,
        )
        if (place.description.isNotBlank()) {
            content.add(
                PlaceDetailsScreenItem.Description(
                    description = place.description,
                ),
            )
        }
        return content
    }
}