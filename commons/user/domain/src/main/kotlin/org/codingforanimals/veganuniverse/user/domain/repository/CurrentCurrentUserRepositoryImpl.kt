package org.codingforanimals.veganuniverse.user.domain.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.user.data.dto.UserDTO
import org.codingforanimals.veganuniverse.user.data.source.UserRemoteDataSource
import org.codingforanimals.veganuniverse.user.data.storage.UserLocalStorage
import org.codingforanimals.veganuniverse.user.domain.mapper.toDomainModel
import org.codingforanimals.veganuniverse.user.domain.model.User

internal class CurrentCurrentUserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalStorage: UserLocalStorage,
) : CurrentUserRepository {
    override suspend fun getCurrentUser(): User? {
        return (userLocalStorage.getCurrentUser() ?: run {
            getAndStoreCurrentUser()
        })?.toDomainModel()
    }

    override suspend fun clearUser() {
        userLocalStorage.clearCurrentUser()
    }

    override suspend fun refreshUser(): User? {
        FirebaseAuth.getInstance().currentUser?.reload()?.await()
        return getAndStoreCurrentUser()?.toDomainModel()
    }

    private suspend fun getAndStoreCurrentUser(): UserDTO? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        return userRemoteDataSource.getCurrentUser(userId)?.also {
            userLocalStorage.setCurrentUser(it)
        }
    }
}
