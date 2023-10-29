package org.codingforanimals.veganuniverse.profile.presentation.model

import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard
import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCardItem

data class Contributions(
    val places: ContributionState<PlaceCard> = ContributionState.Loading,
    val recipes: ContributionState<SimpleCardItem> = ContributionState.Loading,
)

sealed class ContributionState<out T : Any> {
    data object Loading : ContributionState<Nothing>()
    data object Error : ContributionState<Nothing>()
    data class Success<out T : Any>(val items: List<T>) : ContributionState<T>()
}