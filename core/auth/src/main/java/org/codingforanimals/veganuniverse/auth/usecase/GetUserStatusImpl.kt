package org.codingforanimals.veganuniverse.auth.usecase

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.auth.model.User

internal class GetUserStatusImpl(
    private val userRepository: UserRepository,
) : GetUserStatus {
    override operator fun invoke(): Flow<User?> = userRepository.user
    override suspend fun refreshUser(): User? = userRepository.refreshUser()
}