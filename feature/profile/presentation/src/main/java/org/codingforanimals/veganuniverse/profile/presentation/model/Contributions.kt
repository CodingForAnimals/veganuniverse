package org.codingforanimals.veganuniverse.profile.presentation.model

import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard
import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCardItem

data class Contributions(
    val recipes: ProfileFeatureContentState<SimpleCardItem> = ProfileFeatureContentState.Loading,
    val places: ProfileFeatureContentState<PlaceCard> = ProfileFeatureContentState.Loading,
)