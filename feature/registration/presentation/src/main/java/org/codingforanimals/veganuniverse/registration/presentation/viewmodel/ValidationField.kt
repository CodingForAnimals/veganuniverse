package org.codingforanimals.veganuniverse.registration.presentation.viewmodel

import android.util.Patterns
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.ValidationField

data class EmailField(
    val value: String = "",
) : ValidationField() {
    override val isValid: Boolean = Patterns.EMAIL_ADDRESS.matcher(value).matches()
}

data class PasswordField(
    val value: String = "",
) : ValidationField() {

    override val isValid: Boolean = value.isLongEnough

    private val String.isLongEnough: Boolean
        get() = length >= MIN_LENGTH


    companion object {
        private const val MIN_LENGTH = 6
    }
}