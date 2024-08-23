package org.codingforanimals.veganuniverse.user.data.storage

import org.codingforanimals.veganuniverse.user.data.dto.UserDTO

interface UserLocalStorage {
    suspend fun getCurrentUser(): UserDTO?
    suspend fun setCurrentUser(userDTO: UserDTO)
    suspend fun clearCurrentUser()
}
