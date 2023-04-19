package org.codingforanimals.veganuniverse.navigation

import org.codingforanimals.veganuniverse.app.R
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons

enum class TopLevelDestination(
    val unselectedIcon: Icon,
    val selectedIcon: Icon,
    val iconTextId: Int,
    val titleRes: Int
) {
    COMMUNITY(
        unselectedIcon = VUIcons.Community,
        selectedIcon = VUIcons.CommunityFilled,
        iconTextId = R.string.nav_label_community,
        titleRes = R.string.nav_label_community,
    ),
    PLACES(
        unselectedIcon = VUIcons.Places,
        selectedIcon = VUIcons.PlacesFilled,
        iconTextId = R.string.nav_label_places,
        titleRes = R.string.nav_label_places,
    ),
    CREATE(
        unselectedIcon = VUIcons.Create,
        selectedIcon = VUIcons.CreateFilled,
        iconTextId = R.string.nav_label_create,
        titleRes = R.string.nav_label_create,
    ),
    RECIPES(
        unselectedIcon = VUIcons.Recipes,
        selectedIcon = VUIcons.RecipesFilled,
        iconTextId = R.string.nav_label_recipes,
        titleRes = R.string.topbar_title_recipes,
    ),
    PROFILE(
        unselectedIcon = VUIcons.Profile,
        selectedIcon = VUIcons.ProfileFilled,
        iconTextId = R.string.nav_label_profile,
        titleRes = R.string.nav_label_profile,
    )
}