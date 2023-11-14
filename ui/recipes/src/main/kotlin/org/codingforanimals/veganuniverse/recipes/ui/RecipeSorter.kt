package org.codingforanimals.veganuniverse.recipes.ui

import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

enum class RecipeSorter(val label: Int, val icon: Icon) {
    DATE(label = R.string.most_recent, icon = VUIcons.Clock),
    LIKES(label = R.string.likes_amount, icon = VUIcons.Favorite)
}