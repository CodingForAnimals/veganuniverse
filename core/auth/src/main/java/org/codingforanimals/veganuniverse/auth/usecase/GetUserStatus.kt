package org.codingforanimals.veganuniverse.auth.usecase

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.auth.model.User

interface GetUserStatus {
    operator fun invoke(): Flow<User?>
    suspend fun refreshUser(): User?
}