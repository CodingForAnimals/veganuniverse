package org.codingforanimals.veganuniverse.navigation

import org.codingforanimals.veganuniverse.app.R
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons

enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int
) {

    COMMUNITY(
        selectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Community),
        unselectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Community),
        iconTextId = R.string.nav_label_community,
        titleTextId = R.string.app_name,
    ),
    PLACES(
        selectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Places),
        unselectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Places),
        iconTextId = R.string.nav_label_places,
        titleTextId = R.string.nav_label_places,
    ),
    RECIPES(
        selectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Recipes),
        unselectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Recipes),
        iconTextId = R.string.nav_label_recipes,
        titleTextId = R.string.nav_label_recipes,
    ),
}