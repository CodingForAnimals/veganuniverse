package org.codingforanimals.veganuniverse.profile.domain.model

import org.codingforanimals.veganuniverse.places.entity.Place

data class UserFeatureContributions(
    val places: List<Place>,
)