package org.codingforanimals.veganuniverse.commons.user.domain.model

import android.util.Log

enum class UserRole {
    VALIDATOR,
    REGULAR,
    ;

    companion object {
        private const val TAG = "UserRole"
        fun fromString(role: String?): UserRole {
            return runCatching {
                role?.let {
                    UserRole.valueOf(it)
                } ?: REGULAR
            }.getOrElse {
                Log.e(TAG, "Failed mapping UserRole", it)
                REGULAR
            }
        }
    }
}
