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
    MAP(
        selectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Map),
        unselectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Map),
        iconTextId = R.string.nav_label_map,
        titleTextId = R.string.nav_label_map,
    ),
    RECIPES(
        selectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Recipes),
        unselectedIcon = Icon.ImageVectorIcon(VeganUniverseIcons.Recipes),
        iconTextId = R.string.nav_label_recipes,
        titleTextId = R.string.nav_label_recipes,
    ),
}