package org.codingforanimals.veganuniverse.recipe.presentation

import org.codingforanimals.veganuniverse.recipe.model.RecipeSorter
import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

data class RecipeSorterUI(val label: Int, val icon: Icon)

fun RecipeSorter.toUI(): RecipeSorterUI {
    return when (this) {
        RecipeSorter.DATE -> RecipeSorterUI(label = R.string.most_recent, icon = VUIcons.Clock)
        RecipeSorter.LIKES -> RecipeSorterUI(label = R.string.likes_amount, icon = VUIcons.Favorite)
    }
}
