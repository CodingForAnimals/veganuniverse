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
    val titleRes: Int,
) {
    PRODUCTS(
        route = ProductDestination.Home.route,
        unselectedIcon = VUIcons.VeganLogo,
        selectedIcon = VUIcons.VeganLogo,
        titleRes = R.string.nav_label_products,
    ),
    PLACES(
        route = PlaceDestination.Home.route,
        unselectedIcon = VUIcons.Location,
        selectedIcon = VUIcons.LocationFilled,
        titleRes = R.string.nav_label_places,
    ),
    CREATE(
        route = CreateDestination.Home.route,
        unselectedIcon = VUIcons.Create,
        selectedIcon = VUIcons.CreateFilled,
        titleRes = R.string.nav_label_create,
    ),
    RECIPES(
        route = RecipesDestination.Home.route,
        unselectedIcon = VUIcons.Recipes,
        selectedIcon = VUIcons.RecipesFilled,
        titleRes = R.string.nav_label_recipes,
    ),
    PROFILE(
        route = ProfileDestination.Home.route,
        unselectedIcon = VUIcons.Profile,
        selectedIcon = VUIcons.ProfileFilled,
        titleRes = R.string.nav_label_profile,
    ),
    ;
    companion object {
        fun fromRoute(value: String?): TopLevelDestination? {
            return entries.firstOrNull { it.route == value }
        }
    }
}