package org.codingforanimals.veganuniverse.commons.user.data.source

import org.codingforanimals.veganuniverse.commons.user.data.dto.UserDTO

interface UserRemoteDataSource {
    suspend fun createUser(email: String, name: String): UserDTO
    suspend fun getUser(userId: String): UserDTO?
    suspend fun getCurrentUser(): UserDTO?
    suspend fun reloadUser()
    suspend fun sendVerificationEmail()
}
