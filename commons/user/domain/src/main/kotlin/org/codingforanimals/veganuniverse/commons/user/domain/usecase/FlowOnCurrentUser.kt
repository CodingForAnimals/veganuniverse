package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.user.domain.model.User
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository

interface FlowOnCurrentUser {
    operator fun invoke(): Flow<User?>
}

internal class FlowOnCurrentUserImpl(
    private val currentUserRepository: CurrentUserRepository,
) : FlowOnCurrentUser {
    override fun invoke(): Flow<User?> = currentUserRepository.flowOnCurrentUser()
}
