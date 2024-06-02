package org.codingforanimals.veganuniverse.user.data.source

import org.codingforanimals.veganuniverse.user.data.dto.UserDTO

interface UserRemoteDataSource {
    suspend fun getUser(userId: String): UserDTO?
    suspend fun getCurrentUser(userId: String): UserDTO?
}
