package org.codingforanimals.veganuniverse.places.presentation.home.state

sealed class UserLocationState {
    data object Loading : UserLocationState()
    data object Success : UserLocationState()
    data object Unavailable : UserLocationState()
}