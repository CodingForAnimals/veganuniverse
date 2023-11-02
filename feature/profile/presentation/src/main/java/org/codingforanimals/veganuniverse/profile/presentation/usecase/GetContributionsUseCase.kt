package org.codingforanimals.veganuniverse.profile.presentation.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.core.ui.place.PlaceMarker
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.utils.administrativeArea
import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard
import org.codingforanimals.veganuniverse.profile.domain.ContributionsRepository
import org.codingforanimals.veganuniverse.profile.presentation.model.Contributions
import org.codingforanimals.veganuniverse.profile.presentation.model.ProfileFeatureContentState
import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCardItem

private const val TAG = "GetContributionsUseCase"

class GetContributionsUseCase(
    private val contributionsRepository: ContributionsRepository,
) {
    suspend operator fun invoke(userId: String): Contributions {
        val contributions = contributionsRepository.getContributions(userId)
        return Contributions(
            places = getContributedPlaces(contributions.placesIds),
            recipes = getContributedRecipes(contributions.recipesIds),
        )
    }

    private suspend fun getContributedPlaces(placesIds: List<String>): ProfileFeatureContentState<PlaceCard> {
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

    private fun Place.toCard(): PlaceCard? {
        return try {
            val type = PlaceType.valueOf(type)
            PlaceCard(
                geoHash = geoHash,
                name = name,
                rating = rating,
                streetAddress = addressComponents.streetAddress,
                administrativeArea = addressComponents.administrativeArea,
                type = type,
                imageRef = imageRef,
                tags = tags.map { PlaceTag.valueOf(it) },
                timestamp = timestamp,
                latitude = latitude,
                longitude = longitude,
                marker = PlaceMarker.getMarker(type),
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }

    private fun <T> List<T>.getLastTwo(): List<T> {
        val last = getOrNull(size - 1)
        val previous = getOrNull(size - 2)
        return listOfNotNull(last, previous)
    }
}

