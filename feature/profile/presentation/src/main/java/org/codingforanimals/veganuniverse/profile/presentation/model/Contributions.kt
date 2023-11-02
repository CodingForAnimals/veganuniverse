package org.codingforanimals.veganuniverse.profile.presentation.model

import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard
import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCardItem

data class Contributions(
    val places: ProfileFeatureContentState<PlaceCard> = ProfileFeatureContentState.Loading,
    val recipes: ProfileFeatureContentState<SimpleCardItem> = ProfileFeatureContentState.Loading,
)