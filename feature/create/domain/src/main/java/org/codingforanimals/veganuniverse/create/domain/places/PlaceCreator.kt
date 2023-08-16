package org.codingforanimals.veganuniverse.create.domain.places

import org.codingforanimals.veganuniverse.create.domain.places.model.CreatePlaceResult
import org.codingforanimals.veganuniverse.places.entity.PlaceForm

interface PlaceCreator {
    suspend fun submitPlace(form: PlaceForm): CreatePlaceResult
}