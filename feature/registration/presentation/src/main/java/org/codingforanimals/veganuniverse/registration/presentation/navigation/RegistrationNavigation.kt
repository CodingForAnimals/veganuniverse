package org.codingforanimals.veganuniverse.registration.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.registration.presentation.di.registrationModule
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationScreen
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.EmailRegistration
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination.Prompt
import org.codingforanimals.veganuniverse.registration.presentation.prompt.PromptScreen
import org.koin.core.context.loadKoinModules

sealed class RegistrationDestination(route: String) : Destination(route) {
    object EmailRegistration : RegistrationDestination("registration_email")
    object Prompt : RegistrationDestination("registration_prompt")
}

fun NavGraphBuilder.registrationGraph(
    navController: NavController,
    navigateToCommunity: () -> Unit,
) {
    loadKoinModules(registrationModule)

    composable(
        route = Prompt.route,
        content = {
            PromptScreen(
                navigateToEmailRegistration = { navController.navigate(EmailRegistration.route) },
            )
        },
    )

    composable(
        route = EmailRegistration.route,
        content = {
            EmailRegistrationScreen(
                navigateToEmailValidationScreen = { },
            )
        },
    )
}