package org.codingforanimals.veganuniverse.user.domain.usecase

import org.codingforanimals.veganuniverse.user.data.source.UserRemoteDataSource
import org.codingforanimals.veganuniverse.user.domain.mapper.toDomainModel
import org.codingforanimals.veganuniverse.user.domain.model.User

internal class GetUserImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
) : GetUser {
    override suspend fun invoke(userId: String): User? {
        return userRemoteDataSource.getUser(userId)?.toDomainModel()
    }
}
