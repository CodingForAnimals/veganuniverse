package org.codingforanimals.veganuniverse.create.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.create.presentation.CreateScreen
import org.codingforanimals.veganuniverse.create.presentation.thankyou.ThankYouScreen

object CreateDestination : Destination(route = "create_route")
object ThankYouDestination : Destination(route = "thank_you_route")

fun NavGraphBuilder.createGraph(
    navController: NavController,
    navigateToPlaceDetails: () -> Unit,
) {
    composable(
        route = CreateDestination.route
    ) {
        CreateScreen(
            navigateToThankYouScreen = { navController.navigate(ThankYouDestination.route) },
            navigateToAlreadyExistingPlace = navigateToPlaceDetails,
        )
    }
    composable(
        route = ThankYouDestination.route,
    ) {
        ThankYouScreen(
            navigateToCreateScreen = { navController.navigate(CreateDestination.route) },
        )
    }
}