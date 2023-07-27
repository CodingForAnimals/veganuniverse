package org.codingforanimals.veganuniverse.auth.usecase

import kotlinx.coroutines.flow.StateFlow
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.auth.model.User

internal class GetUserStatusImpl(
    private val userRepository: UserRepository,
) : GetUserStatus {
    override operator fun invoke(): StateFlow<User?> = userRepository.user
}