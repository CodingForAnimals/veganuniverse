package org.codingforanimals.veganuniverse.commons.profile.shared.model

sealed class ToggleResult(open val newValue: Boolean) {
    data class UnexpectedError(override val newValue: Boolean) : ToggleResult(newValue)
    data class Success(override val newValue: Boolean) : ToggleResult(newValue)
}