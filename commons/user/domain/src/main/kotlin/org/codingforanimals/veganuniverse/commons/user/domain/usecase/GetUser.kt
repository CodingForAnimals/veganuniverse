package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import org.codingforanimals.veganuniverse.commons.user.data.source.UserRemoteDataSource
import org.codingforanimals.veganuniverse.commons.user.domain.mapper.toDomainModel
import org.codingforanimals.veganuniverse.commons.user.domain.model.User

interface GetUser {
    suspend operator fun invoke(userId: String): User?
}

internal class GetUserImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
) : GetUser {
    override suspend fun invoke(userId: String): User? {
        return userRemoteDataSource.getUser(userId)?.toDomainModel()
    }
}
