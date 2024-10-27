package org.codingforanimals.veganuniverse.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.codingforanimals.veganuniverse.additives.presentation.AdditivesDestination
import org.codingforanimals.veganuniverse.additives.presentation.additivesGraph
import org.codingforanimals.veganuniverse.app.validate.validatorNavigation
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.create.presentation.navigation.createGraph
import org.codingforanimals.veganuniverse.create.presentation.navigation.navigateToThankYouAdditiveScreen
import org.codingforanimals.veganuniverse.create.presentation.navigation.navigateToThankYouPlaceScreen
import org.codingforanimals.veganuniverse.create.presentation.navigation.navigateToThankYouProductScreen
import org.codingforanimals.veganuniverse.create.presentation.navigation.navigateToThankYouRecipeScreen
import org.codingforanimals.veganuniverse.place.presentation.navigation.navigateToPlaceListing
import org.codingforanimals.veganuniverse.place.presentation.navigation.placesGraph
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination
import org.codingforanimals.veganuniverse.product.presentation.navigation.productGraph
import org.codingforanimals.veganuniverse.profile.ProfileDestination
import org.codingforanimals.veganuniverse.profile.profileGraph
import org.codingforanimals.veganuniverse.recipes.presentation.navigateToRecipeListing
import org.codingforanimals.veganuniverse.recipes.presentation.recipesGraph
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination
import org.codingforanimals.veganuniverse.registration.presentation.navigation.registrationGraph

@Composable
internal fun VUAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Destination = ProductDestination.Home,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route,
    ) {
        registrationGraph(
            navController = navController
        )
        productGraph(
            navController = navController,
            navigateToAdditivesBrowsing = {
                navController.navigate(AdditivesDestination.Browsing)
            },
            navigateToThankYouScreen = navController::navigateToThankYouProductScreen
        )
        additivesGraph(
            navController = navController,
            navigateToThankYouScreen = navController::navigateToThankYouAdditiveScreen
        )
        placesGraph(
            navController = navController,
            navigateToThankYouScreen = navController::navigateToThankYouPlaceScreen,
        )
        createGraph(
            navController = navController,
            navigateToProfileScreen = {
                navController.navigate(ProfileDestination.Home.route) {
                    popUpTo(navController.graph.startDestinationId)
                }
            }
        )
        recipesGraph(
            navController = navController,
            navigateToThankYouScreen = navController::navigateToThankYouRecipeScreen
        )
        profileGraph(
            navController = navController,
            navigateToRegister = {
                navController.navigate(RegistrationDestination.AuthPrompt)
            },
            navigateToPlaceListing = { listingType ->
                navController.navigateToPlaceListing(listingType)
            },
            navigateToRecipeListing = { listingType ->
                navController.navigateToRecipeListing(listingType)
            },
            navigateToProductListing = { listingType ->
                navController.navigate(ProductDestination.Listing(listingType))
            }
        )

        validatorNavigation(
            navController = navController,
            navigateToAdditiveEdits = { navController.navigate(AdditivesDestination.EditList) }
        )
    }
}
