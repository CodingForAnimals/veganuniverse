package org.codingforanimals.veganuniverse.core.ui.icons

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.ui.graphics.vector.ImageVector
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.icons.Icon.DrawableResourceIcon
import org.codingforanimals.veganuniverse.core.ui.icons.Icon.ImageVectorIcon

object VUIcons {

    // Bottom nav icons although they can be and are used in other parts as well
    val Community = DrawableResourceIcon(R.drawable.ic_community)
    val CommunityFilled = DrawableResourceIcon(R.drawable.ic_community_filled)
    val Location = DrawableResourceIcon(R.drawable.ic_location)
    val LocationFilled = DrawableResourceIcon(R.drawable.ic_location_filled)
    val Recipes = DrawableResourceIcon(R.drawable.ic_recipes)
    val RecipesFilled = DrawableResourceIcon(R.drawable.ic_recipes_filled)
    val Profile = DrawableResourceIcon(R.drawable.ic_profile)
    val ProfileFilled = DrawableResourceIcon(R.drawable.ic_profile_filled)
    val Create = DrawableResourceIcon(R.drawable.ic_create)
    val CreateFilled = DrawableResourceIcon(R.drawable.ic_create_filled)

    // Generic icons used throughout the app
    val ArrowBack = ImageVectorIcon(Icons.Rounded.ArrowBack)
    val Close = ImageVectorIcon(Icons.Rounded.Close)
    val MoreOptions = ImageVectorIcon(Icons.Default.MoreVert)
    val Settings = DrawableResourceIcon(R.drawable.ic_settings)
    val Delete = DrawableResourceIcon(R.drawable.ic_bin)
    val Bullet = DrawableResourceIcon(R.drawable.ic_bullet)
    val Favorite = DrawableResourceIcon(R.drawable.ic_heart)
    val FavoriteFilled = DrawableResourceIcon(R.drawable.ic_heart_filled)
    val Comment = DrawableResourceIcon(R.drawable.ic_comment)
    val Reply = DrawableResourceIcon(R.drawable.ic_reply)
    val Report = DrawableResourceIcon(R.drawable.ic_flag)
    val Share = DrawableResourceIcon(R.drawable.ic_share)
    val Bookmark = DrawableResourceIcon(R.drawable.ic_bookmark)
    val Filter = DrawableResourceIcon(R.drawable.ic_filter)
    val Sort = DrawableResourceIcon(R.drawable.ic_sort)
    val Notifications = DrawableResourceIcon(R.drawable.ic_notifications)
    val Search = DrawableResourceIcon(R.drawable.ic_search)
    val Star = DrawableResourceIcon(R.drawable.ic_star)
    val StarFilled = DrawableResourceIcon(R.drawable.ic_star_filled)
    val Edit = DrawableResourceIcon(R.drawable.ic_pencil)
    val Pictures = DrawableResourceIcon(R.drawable.ic_pictures)
    val Check = DrawableResourceIcon(R.drawable.ic_check)
    val Add = ImageVectorIcon(Icons.Default.Add)

    // Icons used in tags mainly
    val Restaurant = DrawableResourceIcon(R.drawable.ic_restaurant)
    val Store = DrawableResourceIcon(R.drawable.ic_store)
    val Clock = DrawableResourceIcon(R.drawable.ic_clock)
    val ClockFilled = DrawableResourceIcon(R.drawable.ic_clock_filled)
    val Delivery = DrawableResourceIcon(R.drawable.ic_food_delivery)
    val CoffeeMug = DrawableResourceIcon(R.drawable.ic_coffee_mug)
    val Leaf = DrawableResourceIcon(R.drawable.ic_leaf)
    val VeganLogo = DrawableResourceIcon(R.drawable.ic_vegan_logo)
    val Bag = DrawableResourceIcon(R.drawable.ic_bag)
    val Chairs = DrawableResourceIcon(R.drawable.ic_chairs)
    val Dressing = DrawableResourceIcon(R.drawable.ic_dressing)
    val Fridge = DrawableResourceIcon(R.drawable.ic_fridge)
    val Jar = DrawableResourceIcon(R.drawable.ic_jar)
    val MittenFilled = DrawableResourceIcon(R.drawable.ic_mitten_filled)
    val Nibbles = DrawableResourceIcon(R.drawable.ic_nibbles)
    val GlutenFree = DrawableResourceIcon(R.drawable.ic_gluten_free)
    val DairyFree = DrawableResourceIcon(R.drawable.ic_dairy_free)
    val SaltFree = DrawableResourceIcon(R.drawable.ic_salt_free)
    val SugarFree = DrawableResourceIcon(R.drawable.ic_sugar_free)
    val Shaker = DrawableResourceIcon(R.drawable.ic_shaker)
    val Pie = DrawableResourceIcon(R.drawable.ic_pie)
    val Beer = DrawableResourceIcon(R.drawable.ic_beer)

    // Map related icons
    val Shrink = DrawableResourceIcon(R.drawable.ic_shrink)
    val Expand = DrawableResourceIcon(R.drawable.ic_expand)
    val MarkerCafe = DrawableResourceIcon(R.drawable.ic_marker_cafe)
    val MarkerCafeSelected = DrawableResourceIcon(R.drawable.ic_marker_cafe_selected)
    val MarkerRestaurant = DrawableResourceIcon(R.drawable.ic_marker_restaurant)
    val MarkerRestaurantSelected = DrawableResourceIcon(R.drawable.ic_marker_restaurant_selected)
    val MarkerStore = DrawableResourceIcon(R.drawable.ic_marker_store)
    val MarkerStoreSelected = DrawableResourceIcon(R.drawable.ic_marker_store_selected)
    val ArrowUpward = ImageVectorIcon(Icons.Rounded.ArrowUpward)
    val ArrowDropUp = ImageVectorIcon(Icons.Rounded.ArrowDropUp)
    val ArrowDropDown = ImageVectorIcon(Icons.Rounded.ArrowDropDown)

    // Registration icons
    val Email = DrawableResourceIcon(R.drawable.ic_email)
    val Lock = DrawableResourceIcon(R.drawable.ic_lock)
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}