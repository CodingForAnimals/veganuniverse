package org.codingforanimals.veganuniverse.create.place.domain

import org.codingforanimals.veganuniverse.create.place.domain.model.CreatePlaceResult
import org.codingforanimals.veganuniverse.places.entity.PlaceForm

interface PlaceCreator {
    suspend fun submitPlace(form: PlaceForm): CreatePlaceResult
}