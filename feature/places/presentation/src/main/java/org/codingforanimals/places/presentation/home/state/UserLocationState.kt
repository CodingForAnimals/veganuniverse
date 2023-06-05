package org.codingforanimals.places.presentation.home.state

sealed class UserLocationState {
    object Loading : UserLocationState()
    object Success : UserLocationState()
    object Unavailable : UserLocationState()
}