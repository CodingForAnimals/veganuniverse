package org.codingforanimals.veganuniverse.navigation

import org.codingforanimals.veganuniverse.app.R
import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons

enum class TopLevelDestination(
    val unselectedIcon: Int,
    val selectedIcon: Int,
    val iconTextId: Int,
    val titleTextId: Int
) {
    COMMUNITY(
        unselectedIcon = VeganUniverseIcons.Community,
        selectedIcon = VeganUniverseIcons.CommunitySelected,
        iconTextId = R.string.nav_label_community,
        titleTextId = R.string.app_name,
    ),
    PLACES(
        unselectedIcon = VeganUniverseIcons.Places,
        selectedIcon = VeganUniverseIcons.PlacesSelected,
        iconTextId = R.string.nav_label_places,
        titleTextId = R.string.nav_label_places,
    ),
    CREATE(
        unselectedIcon = VeganUniverseIcons.Create,
        selectedIcon = VeganUniverseIcons.CreateSelected,
        iconTextId = R.string.nav_label_create,
        titleTextId = R.string.nav_label_create,
    ),
    RECIPES(
        unselectedIcon = VeganUniverseIcons.Recipes,
        selectedIcon = VeganUniverseIcons.RecipesSelected,
        iconTextId = R.string.nav_label_recipes,
        titleTextId = R.string.nav_label_recipes,
    ),
    PROFILE(
        unselectedIcon = VeganUniverseIcons.Profile,
        selectedIcon = VeganUniverseIcons.ProfileSelected,
        iconTextId = R.string.nav_label_profile,
        titleTextId = R.string.nav_label_profile,
    )
}