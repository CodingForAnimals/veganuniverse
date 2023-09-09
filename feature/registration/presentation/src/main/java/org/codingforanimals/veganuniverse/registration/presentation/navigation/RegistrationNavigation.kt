package org.codingforanimals.veganuniverse.registration.presentation.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationScreen
import org.codingforanimals.veganuniverse.registration.presentation.emailsignin.EmailSignInScreen
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.EmailRegistration
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.EmailSignIn
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.Prompt
import org.codingforanimals.veganuniverse.registration.presentation.prompt.PromptScreen

sealed class RegistrationDestination(route: String) : Destination(route) {
    data object Prompt : RegistrationDestination("registration_prompt")
    data object EmailRegistration : RegistrationDestination("registration_email_sign_up")
    data object EmailSignIn : RegistrationDestination("registration_email_sign_in")
}

private const val ORIGIN_DESTINATION = "origin_destination_argument"
private val NavBackStackEntry.originDestination: String?
    get() = arguments?.getString(ORIGIN_DESTINATION)

private fun NavController.navigateToOriginDestination(
    backStackEntry: NavBackStackEntry,
    defaultOriginNavigationRoute: String,
) {
    val origin = backStackEntry.originDestination?.let { origin ->
        backQueue
            .map { queueEntry -> queueEntry.destination.route }
            .firstOrNull { route -> route?.contains(origin) == true }
    }
    popBackStack(route = origin ?: defaultOriginNavigationRoute, inclusive = false)
}

fun NavGraphBuilder.registrationGraph(
    navController: NavController,
    defaultOriginNavigationRoute: String,
) {
    composable(
        route = "${Prompt.route}/{$ORIGIN_DESTINATION}",
        arguments = listOf(
            navArgument(ORIGIN_DESTINATION) {
                type = NavType.StringType
            },
        ),
        content = { backStackEntry ->
            val origin = backStackEntry.originDestination ?: defaultOriginNavigationRoute
            PromptScreen(
                navigateUp = navController::navigateUp,
                navigateToEmailRegistration = { navController.navigate("${EmailRegistration.route}/$origin") },
                navigateToEmailSignIn = { navController.navigate("${EmailSignIn.route}/$origin") },
                navigateToOriginDestination = {
                    navController.navigateToOriginDestination(
                        backStackEntry,
                        defaultOriginNavigationRoute
                    )
                }
            )
        },
    )

    composable(
        route = "${EmailRegistration.route}/{$ORIGIN_DESTINATION}",
        content = { backStackEntry ->
            EmailRegistrationScreen(
                navigateUp = navController::navigateUp,
                navigateToEmailValidationScreen = {
                    navController.navigateToOriginDestination(
                        backStackEntry,
                        defaultOriginNavigationRoute
                    )
                },
            )
        },
    )

    composable(
        route = "${EmailSignIn.route}/{$ORIGIN_DESTINATION}",
        content = { backStackEntry ->
            EmailSignInScreen(
                navigateToOriginDestination = {
                    navController.navigateToOriginDestination(
                        backStackEntry,
                        defaultOriginNavigationRoute
                    )
                }
            )
        },
    )
}