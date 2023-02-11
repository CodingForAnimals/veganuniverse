package org.codingforanimals.veganuniverse.user

import kotlinx.coroutines.flow.SharedFlow

interface UserRepository {
    val user: SharedFlow<User?>
    suspend fun login()
    suspend fun logout()
}