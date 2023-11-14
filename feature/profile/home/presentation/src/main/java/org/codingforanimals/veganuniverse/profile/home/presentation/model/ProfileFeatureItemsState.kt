package org.codingforanimals.veganuniverse.profile.home.presentation.model

import org.codingforanimals.veganuniverse.places.ui.PlaceCardItem
import org.codingforanimals.veganuniverse.ui.cards.SimpleCardItem

data class ProfileFeatureItemsState(
    val recipes: ProfileFeatureContentState<SimpleCardItem> = ProfileFeatureContentState.Loading,
    val places: ProfileFeatureContentState<PlaceCardItem> = ProfileFeatureContentState.Loading,
) {
    val hasNoItems: Boolean =
        (recipes as? ProfileFeatureContentState.Success)?.items?.isEmpty() == true
            && (places as? ProfileFeatureContentState.Success)?.items?.isEmpty() == true
}