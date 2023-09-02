package org.codingforanimals.veganuniverse.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.places.entity.utils.administrativeArea
import org.codingforanimals.veganuniverse.services.firebase.entity.PlaceCard

internal class PlaceFormToPlaceCardMapper : OneWayEntityMapper<PlaceForm, PlaceCard> {
    override fun map(obj: PlaceForm): PlaceCard {
        return with(obj) {
            PlaceCard(
                name = name,
                rating = 0.0,
                streetAddress = addressComponents.streetAddress,
                administrativeArea = addressComponents.administrativeArea,
                type = type,
                tags = tags,
            )
        }
    }
}