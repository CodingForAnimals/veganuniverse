package org.codingforanimals.veganuniverse.core.ui.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable

abstract class ValidationField {
    abstract val isValid: Boolean
}

data class PictureField(
    val model: Parcelable? = null,
) : ValidationField() {
    override val isValid: Boolean = when (model) {
        is Bitmap,
        is Uri,
        -> true
        else -> false
    }
}

data class StringField(
    val value: String = "",
) : ValidationField() {
    override val isValid: Boolean = value.isNotBlank()
}

fun areFieldsValid(vararg fields: ValidationField): Boolean {
    for (field in fields) {
        if (!field.isValid) return false
    }
    return true
}