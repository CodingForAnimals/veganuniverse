package org.codingforanimals.veganuniverse.create.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.create.presentation.model.CreateFeature
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.create.presentation.home.CreateHomeScreen
import org.codingforanimals.veganuniverse.create.presentation.thankyou.ThankYouScreen

sealed class CreateDestination(route: String) : Destination(route) {
    data object Home : CreateDestination("create_home_screen")
    data class ThankYou(
        val contributionType: CreateFeature
    ) : CreateDestination("$ROUTE?$CONTRIBUTION_TYPE=${contributionType.name}") {
        companion object {
            internal const val ROUTE = "create_thank_you_screen"
            internal const val CONTRIBUTION_TYPE = "contribution-type"
        }
    }
}

fun NavController.navigateToThankYouAdditiveScreen() {
    navigate(CreateDestination.ThankYou(CreateFeature.ADDITIVE))
}

fun NavController.navigateToThankYouPlaceScreen() {
    navigate(CreateDestination.ThankYou(CreateFeature.PLACE))
}

fun NavController.navigateToThankYouRecipeScreen() {
    navigate(CreateDestination.ThankYou(CreateFeature.RECIPE))
}

fun NavController.navigateToThankYouProductScreen() {
    navigate(CreateDestination.ThankYou(CreateFeature.PRODUCT))
}

fun NavGraphBuilder.createGraph(
    navController: NavController,
    navigateToProfileScreen: () -> Unit,
) {
    composable(
        route = CreateDestination.Home.route,
    ) {
        CreateHomeScreen(
            navigateUp = navController::navigateUp,
        )
    }

    with(CreateDestination.ThankYou) {
        composable(
            route = "$ROUTE?$CONTRIBUTION_TYPE={$CONTRIBUTION_TYPE}",
            arguments = listOf(
                navArgument(CONTRIBUTION_TYPE) {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DeepLink.CreateThankYou.pathWithSchema}/{$CONTRIBUTION_TYPE}"
                }
            )
        ) {
            val contributionType =
                it.arguments?.getString(CONTRIBUTION_TYPE)?.let { contributionType ->
                    CreateFeature.fromString(contributionType)
                }
            ThankYouScreen(
                navigateToCreateScreen = {
                    navController.popBackStack(
                        route = CreateDestination.Home.route,
                        inclusive = false
                    )
                },
                contributionType = contributionType,
                navigateToProfileScreen = navigateToProfileScreen
            )
        }
    }
}