package org.codingforanimals.veganuniverse.auth.usecase

import kotlinx.coroutines.flow.StateFlow
import org.codingforanimals.veganuniverse.auth.model.User

interface GetUserStatus {
    operator fun invoke(): StateFlow<User?>
    suspend fun refreshUser(): User?
}