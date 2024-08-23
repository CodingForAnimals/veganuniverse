package org.codingforanimals.veganuniverse.commons.user.data.source

import org.codingforanimals.veganuniverse.commons.user.data.model.User

interface UserRemoteDataSource {
    suspend fun createUser(email: String, name: String): User
    suspend fun getUser(userId: String): User?
    suspend fun getCurrentUser(): User?
    suspend fun reloadUser()
    suspend fun sendVerificationEmail()
    suspend fun isEmailVerified(): Boolean
}
