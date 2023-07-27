package org.codingforanimals.veganuniverse.registration.presentation.navigation

import android.util.Log
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationScreen
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.EmailRegistration
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.EmailSignIn
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.Prompt
import org.codingforanimals.veganuniverse.registration.presentation.prompt.PromptScreen
import org.codingforanimals.veganuniverse.registration.presentation.signin.EmailSignInScreen

sealed class RegistrationDestination(route: String) : Destination(route) {
    object Prompt : RegistrationDestination("registration_prompt")
    object EmailRegistration : RegistrationDestination("registration_email_sign_up")
    object EmailSignIn : RegistrationDestination("registration_email_sign_in")
}

private const val origin_destination = "origin_destination_argument"
const val origin_sub_destination = "origin_sub_destination_argument"
val NavBackStackEntry.registrationOriginDestination
    get() = arguments?.getString(origin_destination)

fun NavGraphBuilder.registrationGraph(
    navController: NavController,
    navigateToCommunity: () -> Unit,
) {
    composable(
        route = "${Prompt.route}/{$origin_destination}",
        arguments = listOf(
            navArgument(origin_destination) {
                type = NavType.StringType
            },
        ),
        content = { backStackEntry ->
            PromptScreen(
                navigateUp = navController::navigateUp,
                navigateToEmailRegistration = { navController.navigate(EmailRegistration.route) },
                navigateToEmailSignIn = { navController.navigate(EmailSignIn.route) },
                navigateToOriginDestination = {
                    backStackEntry.registrationOriginDestination?.let { origin ->
                        val dest = navController.backQueue.map { it.destination.route }
                            .first { it?.contains(origin) == true }
                        dest?.let {
                            navController.popBackStack(route = it, inclusive = false)
                        } ?: navigateToCommunity()
                    }
                }
            )
        },
    )

    composable(
        route = EmailRegistration.route,
        content = {
            EmailRegistrationScreen(
                navigateUp = navController::navigateUp,
                navigateToEmailValidationScreen = { },
            )
        },
    )

    composable(
        route = EmailSignIn.route,
        content = {
            EmailSignInScreen(
                navigateUp = navController::navigateUp,
                navigateToOriginDestination = {
                    Log.e("pepe", "start destination ${navController.graph.startDestinationRoute}")
                }
            )
        },
    )
}