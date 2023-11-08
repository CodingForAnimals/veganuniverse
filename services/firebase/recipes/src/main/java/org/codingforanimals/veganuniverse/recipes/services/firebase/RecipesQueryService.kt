package org.codingforanimals.veganuniverse.recipes.services.firebase

import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.entity.RecipeQueryParams

interface RecipesQueryService {
    suspend operator fun invoke(params: RecipeQueryParams): List<Recipe>
}

