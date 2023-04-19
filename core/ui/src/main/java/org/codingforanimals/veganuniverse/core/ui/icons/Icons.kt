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
    val Shrink = DrawableResourceIcon(R.drawable.ic_shrink)
    val Expand = DrawableResourceIcon(R.drawable.ic_expand)
    val Bullet = DrawableResourceIcon(R.drawable.ic_bullet)
    val Clock = DrawableResourceIcon(R.drawable.ic_clock)
    val MoreOptions = ImageVectorIcon(Icons.Default.MoreVert)
    val Favorite = DrawableResourceIcon(R.drawable.ic_heart)
    val FavoriteFilled = DrawableResourceIcon(R.drawable.ic_heart_filled)
    val Comment = DrawableResourceIcon(R.drawable.ic_comment)
    val Share = DrawableResourceIcon(R.drawable.ic_share)
    val Bookmark = DrawableResourceIcon(R.drawable.ic_bookmark)
    val Filter = DrawableResourceIcon(R.drawable.ic_filter)
    val Sort = DrawableResourceIcon(R.drawable.ic_sort)
    val Community = DrawableResourceIcon(R.drawable.ic_community)
    val CommunityFilled = DrawableResourceIcon(R.drawable.ic_community_filled)
    val Places = DrawableResourceIcon(R.drawable.ic_places)
    val PlacesFilled = DrawableResourceIcon(R.drawable.ic_places_filled)
    val Recipes = DrawableResourceIcon(R.drawable.ic_recipes)
    val RecipesFilled = DrawableResourceIcon(R.drawable.ic_recipes_filled)
    val Profile = DrawableResourceIcon(R.drawable.ic_profile)
    val ProfileFilled = DrawableResourceIcon(R.drawable.ic_profile_filled)
    val Create = DrawableResourceIcon(R.drawable.ic_create)
    val CreateFilled = DrawableResourceIcon(R.drawable.ic_create_filled)
    val Notifications = DrawableResourceIcon(R.drawable.ic_notifications)
    val Settings = DrawableResourceIcon(R.drawable.ic_settings)
    val Search = DrawableResourceIcon(R.drawable.ic_search)
    val Utensils = DrawableResourceIcon(R.drawable.ic_utensils)
    val Store = DrawableResourceIcon(R.drawable.ic_store)
    val StoreFilled = DrawableResourceIcon(R.drawable.ic_store_filled)
    val Star = DrawableResourceIcon(R.drawable.ic_star)
    val StarFilled = DrawableResourceIcon(R.drawable.ic_star_filled)
    val Reply = DrawableResourceIcon(R.drawable.ic_reply)
    val Report = DrawableResourceIcon(R.drawable.ic_flag)
    val Edit = DrawableResourceIcon(R.drawable.ic_pencil)
    val Delete = DrawableResourceIcon(R.drawable.ic_bin)
    val Pictures = DrawableResourceIcon(R.drawable.ic_pictures)
    val Check = DrawableResourceIcon(R.drawable.ic_check)
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}