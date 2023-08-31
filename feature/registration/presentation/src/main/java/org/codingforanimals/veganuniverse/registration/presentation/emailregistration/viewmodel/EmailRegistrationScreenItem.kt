package org.codingforanimals.veganuniverse.registration.presentation.emailregistration.viewmodel

sealed class EmailRegistrationScreenItem {
    data object ScreenTitle : EmailRegistrationScreenItem()
    data object EmailInputField : EmailRegistrationScreenItem()
    data object UsernameInputField : EmailRegistrationScreenItem()
    data object PasswordInputField : EmailRegistrationScreenItem()
    data object ConfirmPasswordInputField : EmailRegistrationScreenItem()
    data object EmailRegistrationButton : EmailRegistrationScreenItem()
    data object EmailRegistrationWithProviders : EmailRegistrationScreenItem()
}