package org.codingforanimals.veganuniverse.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.create.graph.createGraph
import org.codingforanimals.veganuniverse.place.presentation.navigation.navigateToPlaceListing
import org.codingforanimals.veganuniverse.place.presentation.navigation.placesGraph
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination
import org.codingforanimals.veganuniverse.product.presentation.navigation.productGraph
import org.codingforanimals.veganuniverse.profile.profileGraph
import org.codingforanimals.veganuniverse.recipes.presentation.navigateToRecipeListing
import org.codingforanimals.veganuniverse.recipes.presentation.recipesGraph
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination
import org.codingforanimals.veganuniverse.registration.presentation.navigation.registrationGraph

@Composable
internal fun VUAppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    startDestination: Destination = ProductDestination.Home,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        profileGraph(
            navigateToRegister = {
                navController.navigate(RegistrationDestination.Prompt)
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
        productGraph(
            navController = navController,
            navigateToAuthenticateScreen = {
                navController.navigate(RegistrationDestination.Prompt)
            },
            navigateToAuthScreen = {
                navController.navigate(RegistrationDestination.Prompt)
            },
            snackbarHostState = snackbarHostState
        )
        registrationGraph(
            navController = navController
        )
        placesGraph(
            navController = navController,
            navigateToAuthenticateScreen = {
                navController.navigate(RegistrationDestination.Prompt)
            },
            navigateToReauthenticateScreen = {
                navController.navigate(RegistrationDestination.EmailReauthentication)
            }
        )
        createGraph(
            navController = navController,
            navigateToAuthenticationScreen = {
                navController.navigate(RegistrationDestination.Prompt)
            }
        )
        recipesGraph(
            navController = navController,
            navigateToAuthenticateScreen = {
                navController.navigate(RegistrationDestination.Prompt)
            }
        )
    }
}
