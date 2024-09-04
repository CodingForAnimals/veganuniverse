package org.codingforanimals.veganuniverse.commons.create.domain.model

import android.util.Log

enum class CreateFeature {
    PRODUCT,
    PLACE,
    RECIPE,
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