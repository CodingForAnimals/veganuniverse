package org.codingforanimals.veganuniverse.user.domain.repository

import org.codingforanimals.veganuniverse.user.domain.model.User

interface CurrentUserRepository {
    suspend fun getCurrentUser(): User?
    suspend fun clearUser()
    suspend fun refreshUser(): User?
}

