package org.codingforanimals.veganuniverse.create.presentation.model

import android.util.Log

enum class CreateFeature {
    PRODUCT,
    PLACE,
    RECIPE,
    ADDITIVE,
    ;

    companion object {
        private const val TAG = "CreateFeature"
        fun fromString(value: String?): CreateFeature? {
            return value?.let {
                runCatching {
                    valueOf(it)
                }.onFailure {
                    Log.e(TAG, "fromString: $value", it)
                }.getOrNull()
            }
        }
    }
}
