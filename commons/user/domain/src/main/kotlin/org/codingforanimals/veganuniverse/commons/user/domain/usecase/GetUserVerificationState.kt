package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.user.domain.model.User
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository

class GetUserVerificationState(
    private val currentUserRepository: CurrentUserRepository,
) {
    suspend operator fun invoke(): State {
        val user = currentUserRepository.flowOnCurrentUser().firstOrNull() ?: return State.Unauthenticated
        return State.Verified(user).takeIf {
            currentUserRepository.flowOnIsUserVerified().firstOrNull() == true
        } ?: State.Unverified(user)
    }

    sealed class State {
        data object Unauthenticated : State()
        data class Unverified(val user: User) : State()
        data class Verified(val user: User) : State()
    }
}
