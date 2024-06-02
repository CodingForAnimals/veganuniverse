package org.codingforanimals.veganuniverse.user.domain.usecase

import org.codingforanimals.veganuniverse.user.domain.model.User
import org.codingforanimals.veganuniverse.user.domain.repository.CurrentUserRepository

internal class GetCurrentUserImpl(
    private val currentUserRepository: CurrentUserRepository,
) : GetCurrentUser {
    override suspend fun invoke(): User? {
        return currentUserRepository.getCurrentUser()
    }
}
