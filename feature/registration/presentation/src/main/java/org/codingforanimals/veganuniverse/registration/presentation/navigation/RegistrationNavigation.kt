package org.codingforanimals.veganuniverse.registration.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationScreen
import org.codingforanimals.veganuniverse.registration.presentation.emailsignin.EmailSignInScreen
import org.codingforanimals.veganuniverse.registration.presentation.emailvalidation.ValidateEmailPromptScreen
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.AuthPrompt
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.EmailRegistration
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.EmailSignIn
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.ValidateEmailPrompt
import org.codingforanimals.veganuniverse.registration.presentation.prompt.PromptScreen
import org.codingforanimals.veganuniverse.registration.presentation.reauthentication.EmailValidatedScreen

sealed class RegistrationDestination(route: String) : Destination(route) {
    data object AuthPrompt : RegistrationDestination("authentication_prompt")
    data object EmailRegistration : RegistrationDestination("email_sign_up")
    data object ValidateEmailPrompt : RegistrationDestination("validate_email_prompt")
    data object EmailSignIn : RegistrationDestination("email_sign_in")
    data object EmailValidated : RegistrationDestination("email_validated")
}

fun NavGraphBuilder.registrationGraph(
    navController: NavController,
) {

    composable(
        route = AuthPrompt.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DeepLink.AuthPrompt.deeplink
            }
        ),
        content = {
            PromptScreen(
                navigateUp = navController::navigateUp,
                navigateToEmailRegistration = { navController.navigate(EmailRegistration) },
                navigateToEmailSignIn = { navController.navigate(EmailSignIn) },
                navigateToOriginDestination = navController::navigateUp,
            )
        },
    )

    composable(
        route = EmailRegistration.route,
        content = {
            EmailRegistrationScreen(
                navigateUp = navController::navigateUp,
                navigateToEmailValidationScreen = {
                    navController.popBackStack(
                        route = AuthPrompt.route,
                        inclusive = true
                    )
                }
            )
        },
    )

    composable(
        route = ValidateEmailPrompt.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DeepLink.ValidateEmailPrompt.deeplink
            }
        ),
        content = {
            ValidateEmailPromptScreen(
                navigateToEmailSignIn = {
                    navController.navigate(EmailSignIn.route) {
                        popUpTo(ValidateEmailPrompt.route) {
                            inclusive = true
                        }
                    }
                },
                navigateUp = navController::navigateUp
            )
        },
    )

    composable(
        route = EmailSignIn.route,
        content = {
            EmailSignInScreen(
                navigateUp = navController::navigateUp,
                navigateToOriginDestination = {
                    val navigatedBackToPrompt = navController.popBackStack(
                        route = AuthPrompt.route,
                        inclusive = true
                    )
                    if (!navigatedBackToPrompt) {
                        navController.navigateUp()
                    }
                }
            )
        },
    )

    composable(
        route = RegistrationDestination.EmailValidated.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DeepLink.Reauthentication.deeplink
            }
        )
    ) {
        EmailValidatedScreen(
            navigateUp = navController::navigateUp,
            navigateToEmailSignIn = { navController.navigate(EmailSignIn) },
        )
    }
}
