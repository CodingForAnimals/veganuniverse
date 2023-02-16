package org.codingforanimals.veganuniverse.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val user: User
    suspend fun isUserLoggedIn(): Flow<User>
    suspend fun login(): Flow<User>
    suspend fun logout(): Flow<User>
}