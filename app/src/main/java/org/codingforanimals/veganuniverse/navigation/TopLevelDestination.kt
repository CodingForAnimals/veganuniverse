package org.codingforanimals.veganuniverse.navigation

import org.codingforanimals.veganuniverse.app.R
import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons

enum class TopLevelDestination(
    val unselectedIcon: Int,
    val selectedIcon: Int,
    val iconTextId: Int,
    val titleRes: Int
) {
    COMMUNITY(
        unselectedIcon = VeganUniverseIcons.Community,
        selectedIcon = VeganUniverseIcons.CommunitySelected,
        iconTextId = R.string.nav_label_community,
        titleRes = R.string.nav_label_community,
    ),
    PLACES(
        unselectedIcon = VeganUniverseIcons.Places,
        selectedIcon = VeganUniverseIcons.PlacesSelected,
        iconTextId = R.string.nav_label_places,
        titleRes = R.string.nav_label_places,
    ),
    CREATE(
        unselectedIcon = VeganUniverseIcons.Create,
        selectedIcon = VeganUniverseIcons.CreateSelected,
        iconTextId = R.string.nav_label_create,
        titleRes = R.string.nav_label_create,
    ),
    RECIPES(
        unselectedIcon = VeganUniverseIcons.Recipes,
        selectedIcon = VeganUniverseIcons.RecipesSelected,
        iconTextId = R.string.nav_label_recipes,
        titleRes = R.string.nav_label_recipes,
    ),
    PROFILE(
        unselectedIcon = VeganUniverseIcons.Profile,
        selectedIcon = VeganUniverseIcons.ProfileSelected,
        iconTextId = R.string.nav_label_profile,
        titleRes = R.string.nav_label_profile,
    )
}