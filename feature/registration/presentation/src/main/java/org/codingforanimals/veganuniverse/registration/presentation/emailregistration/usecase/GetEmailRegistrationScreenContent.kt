package org.codingforanimals.veganuniverse.registration.presentation.emailregistration.usecase

import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.viewmodel.EmailRegistrationScreenItem

class GetEmailRegistrationScreenContent {
    operator fun invoke() = listOf(
        EmailRegistrationScreenItem.ScreenTitle,
        EmailRegistrationScreenItem.EmailInputField,
        EmailRegistrationScreenItem.UsernameInputField,
        EmailRegistrationScreenItem.PasswordInputField,
        EmailRegistrationScreenItem.ConfirmPasswordInputField,
        EmailRegistrationScreenItem.EmailRegistrationButton,
        EmailRegistrationScreenItem.EmailRegistrationWithProviders,
    )
}