package org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel

sealed class AuthProvider {
    object Gmail : AuthProvider()
    object Facebook : AuthProvider()
    object Twitter : AuthProvider()
}
