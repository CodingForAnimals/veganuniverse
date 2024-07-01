package org.codingforanimals.veganuniverse.commons.user.data.storage

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.user.data.dto.UserDTO

interface UserLocalStorage {
    fun flowOnCurrentUser(): Flow<UserDTO?>
    suspend fun setCurrentUser(userDTO: UserDTO)
    suspend fun clearCurrentUser()
}
