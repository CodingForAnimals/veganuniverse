package org.codingforanimals.veganuniverse.profile.home.presentation.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.places.ui.PlaceCardItem
import org.codingforanimals.veganuniverse.profile.home.domain.ContributionsRepository
import org.codingforanimals.veganuniverse.profile.home.presentation.entity.toCard
import org.codingforanimals.veganuniverse.profile.home.presentation.model.ProfileFeatureContentState
import org.codingforanimals.veganuniverse.profile.home.presentation.model.ProfileFeatureItemsState
import org.codingforanimals.veganuniverse.ui.cards.SimpleCardItem

private const val TAG = "GetContributionsUseCase"

class GetContributionsUseCase(
    private val contributionsRepository: ContributionsRepository,
) {
    suspend operator fun invoke(userId: String): ProfileFeatureItemsState {
        val contributions = contributionsRepository.getContributions(userId)
        return ProfileFeatureItemsState(
            places = getContributedPlaces(contributions.placesIds),
            recipes = getContributedRecipes(contributions.recipesIds),
        )
    }

    private suspend fun getContributedPlaces(placesIds: List<String>): ProfileFeatureContentState<PlaceCardItem> {
        return try {
            val lastTwoContributions = placesIds.getLastTwo()
            val cards = contributionsRepository
                .getContributedPlaces(lastTwoContributions)
                .mapNotNull { it.toCard() }
            ProfileFeatureContentState.Success(cards)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            ProfileFeatureContentState.Error
        }
    }

    private suspend fun getContributedRecipes(recipesIds: List<String>): ProfileFeatureContentState<SimpleCardItem> {
        return try {
            val lastTwoContributions = recipesIds.getLastTwo()
            val items = contributionsRepository
                .getContributedRecipes(lastTwoContributions)
                .map { SimpleCardItem(id = it.id, title = it.title, imageRef = it.imageRef) }
            ProfileFeatureContentState.Success(items)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            ProfileFeatureContentState.Error
        }
    }

    private fun <T> List<T>.getLastTwo(): List<T> {
        val last = getOrNull(size - 1)
        val previous = getOrNull(size - 2)
        return listOfNotNull(last, previous)
    }
}

