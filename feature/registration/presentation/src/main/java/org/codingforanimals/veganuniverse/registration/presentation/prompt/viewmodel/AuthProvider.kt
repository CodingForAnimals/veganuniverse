package org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel

sealed class AuthProvider {
    data object Gmail : AuthProvider()
}
