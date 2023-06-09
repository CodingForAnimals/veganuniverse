package org.codingforanimals.veganuniverse.create.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.create.domain.createContentModule
import org.codingforanimals.veganuniverse.create.presentation.CreateScreen
import org.codingforanimals.veganuniverse.create.presentation.place.di.createPlaceModule
import org.codingforanimals.veganuniverse.create.presentation.thankyou.ThankYouScreen
import org.koin.core.context.loadKoinModules

object CreateDestination : Destination(route = "create_route")
object ThankYouDestination : Destination(route = "thank_you_route")

fun NavGraphBuilder.createGraph(
    navController: NavController,
) {
    loadKoinModules(
        listOf(
            createContentModule,
            createPlaceModule
        )
    )
    composable(
        route = CreateDestination.route
    ) {
        CreateScreen(
            navigateToThankYouScreen = { navController.navigate(ThankYouDestination.route) }
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