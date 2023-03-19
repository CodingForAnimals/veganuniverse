package org.codingforanimals.veganuniverse.core.ui.icons

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.ui.graphics.vector.ImageVector
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.icons.Icon.DrawableResourceIcon
import org.codingforanimals.veganuniverse.core.ui.icons.Icon.ImageVectorIcon

object VUIcons {
    val ArrowBack = ImageVectorIcon(Icons.Rounded.ArrowBack)
    val Close = ImageVectorIcon(Icons.Rounded.Close)

    val Bullet = DrawableResourceIcon(R.drawable.ic_bullet)
    val Clock = DrawableResourceIcon(R.drawable.ic_clock)
    val MoreOptions = ImageVectorIcon(Icons.Default.MoreVert)
    val Favorite = DrawableResourceIcon(R.drawable.ic_favorite)
    val Comment = DrawableResourceIcon(R.drawable.ic_comment)
    val Share = DrawableResourceIcon(R.drawable.ic_share)
    val Bookmark = DrawableResourceIcon(R.drawable.ic_bookmark)
    val Filter = DrawableResourceIcon(R.drawable.ic_filter)
    val Sort = DrawableResourceIcon(R.drawable.ic_sort)
    val Community = DrawableResourceIcon(R.drawable.ic_community)
    val CommunitySelected = DrawableResourceIcon(R.drawable.ic_community_selected)
    val Places = DrawableResourceIcon(R.drawable.ic_places)
    val PlacesSelected = DrawableResourceIcon(R.drawable.ic_places_selected)
    val Recipes = DrawableResourceIcon(R.drawable.ic_recipes)
    val RecipesSelected = DrawableResourceIcon(R.drawable.ic_recipes_selected)
    val Profile = DrawableResourceIcon(R.drawable.ic_profile)
    val ProfileSelected = DrawableResourceIcon(R.drawable.ic_profile_selected)
    val Create = DrawableResourceIcon(R.drawable.ic_create)
    val CreateSelected = DrawableResourceIcon(R.drawable.ic_create_selected)
    val Notifications = DrawableResourceIcon(R.drawable.ic_notifications)
    val Settings = DrawableResourceIcon(R.drawable.ic_settings)
    val Search = DrawableResourceIcon(R.drawable.ic_search)
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}