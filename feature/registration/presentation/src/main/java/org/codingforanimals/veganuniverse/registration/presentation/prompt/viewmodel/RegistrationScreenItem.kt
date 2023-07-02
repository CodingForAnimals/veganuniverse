package org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel

sealed class RegistrationScreenItem {
    object Title : RegistrationScreenItem()
    object Text : RegistrationScreenItem()
    object RegisterButton : RegistrationScreenItem()
    object SignInButton : RegistrationScreenItem()
    object Providers : RegistrationScreenItem()
}