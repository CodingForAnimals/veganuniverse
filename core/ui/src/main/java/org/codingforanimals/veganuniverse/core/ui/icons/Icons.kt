package org.codingforanimals.veganuniverse.core.ui.icons

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector
import org.codingforanimals.veganuniverse.core.ui.R

object VeganUniverseIcons {
    val ArrowBack = Icons.Rounded.ArrowBack
    val Community = R.drawable.ic_community
    val CommunitySelected = R.drawable.ic_community_selected
    val Places = R.drawable.ic_places
    val PlacesSelected = R.drawable.ic_places_selected
    val Recipes = R.drawable.ic_recipes
    val RecipesSelected = R.drawable.ic_recipes_selected
    val Profile = R.drawable.ic_profile
    val ProfileSelected = R.drawable.ic_profile_selected
    val Create = R.drawable.ic_create
    val CreateSelected = R.drawable.ic_create_selected
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}