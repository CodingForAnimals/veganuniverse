package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.lifecycle.ViewModel
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.RecipeTagLayoutType
import org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag

internal class RecipesHomeViewModel : ViewModel() {
    val content = listOf(
        RecipeHomeScreenItem.Carousel(sorter = RecipeSorter.LIKES),
        RecipeHomeScreenItem.Container(
            tag = RecipeTag.BREAKFAST_AND_EVENING,
            layoutType = RecipeTagLayoutType.FULL_COLUMN_RIGHT
        ),
        RecipeHomeScreenItem.Container(
            tag = RecipeTag.QUICK_RECIPE,
            layoutType = RecipeTagLayoutType.FULL_COLUMN_LEFT
        ),
        RecipeHomeScreenItem.Container(
            tag = RecipeTag.LUNCH_AND_DINNER,
            layoutType = RecipeTagLayoutType.FULL_COLUMN_RIGHT
        ),
    )
}

internal sealed class RecipeHomeScreenItem {
    data class Carousel(val sorter: RecipeSorter) : RecipeHomeScreenItem()
    data class Container(val tag: RecipeTag, val layoutType: RecipeTagLayoutType) :
        RecipeHomeScreenItem()
}