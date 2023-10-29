package org.codingforanimals.veganuniverse.recipes.services

import org.codingforanimals.veganuniverse.recipes.entity.Recipe

interface FetchRecipeService {
    suspend operator fun invoke(id: String): Recipe?
    suspend operator fun invoke(id: List<String>): List<Recipe>
}