package org.codingforanimals.veganuniverse.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val user: Flow<User?>
    suspend fun login()
    suspend fun logout()
}