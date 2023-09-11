package org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase

import org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel.RegistrationScreenItem

class GetPromptScreenContentUseCase {
    operator fun invoke() = listOf(
        RegistrationScreenItem.Title,
        RegistrationScreenItem.Text,
        RegistrationScreenItem.RegisterButton,
        RegistrationScreenItem.SignInButton,
        RegistrationScreenItem.ProvidersDivider,
        RegistrationScreenItem.Providers,
    )
}