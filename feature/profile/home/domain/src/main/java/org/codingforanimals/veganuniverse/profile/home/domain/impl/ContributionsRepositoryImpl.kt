package org.codingforanimals.veganuniverse.profile.home.domain.impl

import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.services.firebase.FetchPlaceService
import org.codingforanimals.veganuniverse.profile.home.domain.ContributionsRepository
import org.codingforanimals.veganuniverse.profile.home.domain.model.Contributions
import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.services.firebase.FetchRecipeService

internal class ContributionsRepositoryImpl(
    private val profileLookupsService: ProfileLookupsService,
    private val fetchPlaces: FetchPlaceService,
    private val fetchRecipes: FetchRecipeService,
) : ContributionsRepository {

    override suspend fun getContributions(userId: String): Contributions {
        val placesIds = profileLookupsService.getContentSavedByUser(
            saveableType = SaveableType.CONTRIBUTION,
            contentType = SaveableContentType.PLACE,
            userId = userId,
        )

        val recipesIds = profileLookupsService.getContentSavedByUser(
            saveableType = SaveableType.CONTRIBUTION,
            contentType = SaveableContentType.RECIPE,
            userId = userId,
        )

        return Contributions(
            placesIds = placesIds,
            recipesIds = recipesIds,
        )
    }

    override suspend fun getContributedPlaces(placesIds: List<String>): List<Place> {
        return if (placesIds.isNotEmpty()) fetchPlaces.byIds(placesIds) else emptyList()
    }

    override suspend fun getContributedRecipes(recipesIds: List<String>): List<Recipe> {
        return if (recipesIds.isNotEmpty()) fetchRecipes.byIds(recipesIds) else emptyList()
    }
}