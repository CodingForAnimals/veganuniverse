package org.codingforanimals.veganuniverse.places.presentation.details.usecase

import org.codingforanimals.veganuniverse.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.veganuniverse.places.presentation.entity.Place

internal class GetPlaceDetailsScreenContent {
    operator fun invoke(place: Place): List<PlaceDetailsScreenItem> {
        return listOfNotNull(
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
            takeIf { place.description.isNotBlank() }?.let {
                PlaceDetailsScreenItem.Description(
                    description = place.description,
                )
            },
            PlaceDetailsScreenItem.Tags(
                tags = place.tags,
            ),
            PlaceDetailsScreenItem.StaticMap(
                marker = place.marker,
                latLng = place.state.position,
            ),
            PlaceDetailsScreenItem.Reviews,
        )
    }
}