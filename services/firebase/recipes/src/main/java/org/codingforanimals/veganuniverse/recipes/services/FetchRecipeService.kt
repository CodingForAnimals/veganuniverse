package org.codingforanimals.veganuniverse.recipes.services

import org.codingforanimals.veganuniverse.recipes.entity.Recipe

interface FetchRecipeService {
    suspend fun byId(id: String): Recipe?
    suspend fun byIds(ids: List<String>): List<Recipe>
}