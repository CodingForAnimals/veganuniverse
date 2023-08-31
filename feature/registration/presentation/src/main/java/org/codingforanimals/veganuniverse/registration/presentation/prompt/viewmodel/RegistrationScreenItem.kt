package org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel

sealed class RegistrationScreenItem {
    data object Title : RegistrationScreenItem()
    data object Text : RegistrationScreenItem()
    data object RegisterButton : RegistrationScreenItem()
    data object SignInButton : RegistrationScreenItem()
    data object Providers : RegistrationScreenItem()
}