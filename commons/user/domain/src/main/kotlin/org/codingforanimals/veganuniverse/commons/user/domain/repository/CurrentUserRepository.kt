package org.codingforanimals.veganuniverse.commons.user.domain.repository

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.user.domain.model.User

interface CurrentUserRepository {
    fun flowOnCurrentUser(): Flow<User?>
    suspend fun createUser(email: String, name: String)
    suspend fun clearUser()
    suspend fun reloadUser()
    suspend fun sendVerificationEmail()
}

