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
    }

    override suspend fun clearUser() {
        userLocalStorage.clearCurrentUser()
    }

    override suspend fun reloadUser() {
        userRemoteDataSource.reloadUser()
        userRemoteDataSource.getCurrentUser()?.also {
            userLocalStorage.setCurrentUser(it)
        }
    }

    override suspend fun sendVerificationEmail() {
        userRemoteDataSource.sendVerificationEmail()
    }
}
