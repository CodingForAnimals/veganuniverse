package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transform
import org.codingforanimals.veganuniverse.commons.user.domain.model.User
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository

interface FlowOnCurrentUser {
    operator fun invoke(refreshUserIfEmailIsUnverified: Boolean = false): Flow<User?>
}

internal class FlowOnCurrentUserImpl(
    private val currentUserRepository: CurrentUserRepository,
) : FlowOnCurrentUser {
    override fun invoke(refreshUserIfEmailIsUnverified: Boolean): Flow<User?> {
        return if (!refreshUserIfEmailIsUnverified) {
            currentUserRepository.flowOnCurrentUser()
        } else {
            currentUserRepository.flowOnCurrentUser().transform { user ->
                if (user?.isVerified == true) {
                    emit(user)
                } else {
                    currentUserRepository.reloadUser()
                    emitAll(currentUserRepository.flowOnCurrentUser())
                }
            }
        }
    }
}