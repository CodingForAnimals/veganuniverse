package org.codingforanimals.veganuniverse.create.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.create.presentation.CreateScreen
import org.codingforanimals.veganuniverse.create.presentation.thankyou.ThankYouScreen
import org.codingforanimals.veganuniverse.ui.navigation.Destination

sealed class CreateDestination(route: String) : Destination(route) {
    data object Home : CreateDestination(route = "create_route")
    data object ThankYouDestination : CreateDestination(route = "thank_you_route")
}

fun NavGraphBuilder.createGraph(
    navController: NavController,
    navigateToPlaceDetails: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
) {
    composable(
        route = CreateDestination.Home.route
    ) {
        CreateScreen(
            navigateToThankYouScreen = { navController.navigate(CreateDestination.ThankYouDestination.route) },
            navigateToAlreadyExistingPlace = navigateToPlaceDetails,
            navigateToAuthenticationScreen = navigateToAuthenticateScreen,
        )
    }
    composable(
        route = CreateDestination.ThankYouDestination.route,
    ) {
        ThankYouScreen(
            navigateToCreateScreen = { navController.navigate(CreateDestination.Home.route) },
        )
    }
}