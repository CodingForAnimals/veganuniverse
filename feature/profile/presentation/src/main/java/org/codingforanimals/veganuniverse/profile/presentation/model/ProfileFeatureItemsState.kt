package org.codingforanimals.veganuniverse.profile.presentation.model

import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard
import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCardItem

data class ProfileFeatureItemsState(
    val recipes: ProfileFeatureContentState<SimpleCardItem> = ProfileFeatureContentState.Loading,
    val places: ProfileFeatureContentState<PlaceCard> = ProfileFeatureContentState.Loading,
) {
    val hasNoItems: Boolean =
        (recipes as? ProfileFeatureContentState.Success)?.items?.isEmpty() == true
            && (places as? ProfileFeatureContentState.Success)?.items?.isEmpty() == true
}