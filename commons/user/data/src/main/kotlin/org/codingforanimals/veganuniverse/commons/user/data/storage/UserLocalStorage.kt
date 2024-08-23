package org.codingforanimals.veganuniverse.commons.user.data.storage

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.user.data.model.User

interface UserLocalStorage {
    fun flowOnCurrentUser(): Flow<User?>
    suspend fun setCurrentUser(user: User)
    suspend fun clearCurrentUser()
}
