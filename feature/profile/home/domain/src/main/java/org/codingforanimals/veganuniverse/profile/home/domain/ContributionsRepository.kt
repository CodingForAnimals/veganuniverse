package org.codingforanimals.veganuniverse.profile.home.domain

import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.profile.home.domain.model.Contributions
import org.codingforanimals.veganuniverse.recipes.entity.Recipe

interface ContributionsRepository {
    suspend fun getContributions(userId: String): Contributions
    suspend fun getContributedPlaces(placesIds: List<String>): List<Place>
    suspend fun getContributedRecipes(recipesIds: List<String>): List<Recipe>
}

