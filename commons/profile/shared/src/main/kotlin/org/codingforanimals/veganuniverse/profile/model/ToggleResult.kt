package org.codingforanimals.veganuniverse.profile.model

sealed class ToggleResult(open val newValue: Boolean) {
    data class GuestUser(override val newValue: Boolean) : ToggleResult(newValue)
    data class UnexpectedError(override val newValue: Boolean) : ToggleResult(newValue)
    data class Success(override val newValue: Boolean) : ToggleResult(newValue)
}
