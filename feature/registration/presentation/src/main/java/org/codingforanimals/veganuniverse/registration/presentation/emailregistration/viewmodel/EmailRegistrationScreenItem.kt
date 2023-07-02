package org.codingforanimals.veganuniverse.registration.presentation.emailregistration.viewmodel

sealed class EmailRegistrationScreenItem {
    object ScreenTitle : EmailRegistrationScreenItem()
    object EmailInputField : EmailRegistrationScreenItem()
    object UsernameInputField : EmailRegistrationScreenItem()
    object PasswordInputField : EmailRegistrationScreenItem()
    object ConfirmPasswordInputField : EmailRegistrationScreenItem()
    object EmailRegistrationButton : EmailRegistrationScreenItem()
    object EmailRegistrationWithProviders : EmailRegistrationScreenItem()
}