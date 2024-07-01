package org.codingforanimals.veganuniverse.navigation

import org.codingforanimals.veganuniverse.app.R
import org.codingforanimals.veganuniverse.create.graph.CreateDestination
import org.codingforanimals.veganuniverse.place.presentation.navigation.PlaceDestination
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination
import org.codingforanimals.veganuniverse.profile.ProfileDestination
import org.codingforanimals.veganuniverse.recipes.presentation.RecipesDestination
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

enum class TopLevelDestination(
    val route: String,
    val unselectedIcon: Icon,
    val selectedIcon: Icon,
    val iconTextId: Int,
    val titleRes: Int,
) {
    PRODUCTS(
        route = ProductDestination.Home.route,
        unselectedIcon = VUIcons.Leaf,
        selectedIcon = VUIcons.Leaf,
        iconTextId = R.string.nav_label_is_vegan,
        titleRes = R.string.nav_label_is_vegan,
    ),
    PLACES(
        route = PlaceDestination.Home.route,
        unselectedIcon = VUIcons.Location,
        selectedIcon = VUIcons.LocationFilled,
        iconTextId = R.string.nav_label_places,
        titleRes = R.string.nav_label_places,
    ),
    CREATE(
        route = CreateDestination.Home.route,
        unselectedIcon = VUIcons.Create,
        selectedIcon = VUIcons.CreateFilled,
        iconTextId = R.string.nav_label_create,
        titleRes = R.string.nav_label_create,
    ),
    RECIPES(
        route = RecipesDestination.Home.route,
        unselectedIcon = VUIcons.Recipes,
        selectedIcon = VUIcons.RecipesFilled,
        iconTextId = R.string.nav_label_recipes,
        titleRes = R.string.topbar_title_recipes,
    ),
    PROFILE(
        route = ProfileDestination.Home.route,
        unselectedIcon = VUIcons.Profile,
        selectedIcon = VUIcons.ProfileFilled,
        iconTextId = R.string.nav_label_profile,
        titleRes = R.string.nav_label_profile,
    )
}