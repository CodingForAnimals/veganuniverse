package org.codingforanimals.veganuniverse.commons.user.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.codingforanimals.veganuniverse.commons.user.data.source.UserRemoteDataSource
import org.codingforanimals.veganuniverse.commons.user.data.storage.UserLocalStorage
import org.codingforanimals.veganuniverse.commons.user.domain.mapper.toDomainModel
import org.codingforanimals.veganuniverse.commons.user.domain.model.User

internal class CurrentCurrentUserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalStorage: UserLocalStorage,
) : CurrentUserRepository {
    override fun flowOnCurrentUser(): Flow<User?> {
        return userLocalStorage.flowOnCurrentUser().map { it?.toDomainModel() }
    }

    override suspend fun createUser(email: String, name: String) {
        val dto = userRemoteDataSource.createUser(email, name)
        userLocalStorage.setCurrentUser(dto)
        userLocalStorage.setIsVerified(userRemoteDataSource.isEmailVerified())
    }

    override suspend fun clearUserLocalStorage() {
        userLocalStorage.clear()
    }

    override suspend fun getAndStoreUser() {
        userRemoteDataSource.getCurrentUser()?.also { remoteUser ->
            userLocalStorage.setCurrentUser(remoteUser)
            userLocalStorage.setIsVerified(userRemoteDataSource.isEmailVerified())
        }
    }

    override suspend fun sendVerificationEmail() {
        userRemoteDataSource.sendVerificationEmail()
    }

    override fun flowOnIsUserVerified(): Flow<Boolean?> {
        return userLocalStorage.flowOnIsVerified()
    }

    override suspend fun refreshIsUserVerified(): Boolean {
        val isUserVerifiedRemote = userRemoteDataSource.isEmailVerified()
        userLocalStorage.setIsVerified(isUserVerifiedRemote)
        return isUserVerifiedRemote
    }
}
