package org.codingforanimals.veganuniverse.recipes.presentation.browsing.model

import org.codingforanimals.veganuniverse.recipes.entity.Recipe

sealed class GetRecipesStatus {
    data object Loading : GetRecipesStatus()
    data object Error : GetRecipesStatus()
    data class Success(val content: List<Recipe>) : GetRecipesStatus()
}