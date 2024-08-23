package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository

class GetUserVerificationState(
    private val currentUserRepository: CurrentUserRepository,
) {
    suspend operator fun invoke(): State {
        currentUserRepository.flowOnCurrentUser().firstOrNull() ?: return State.Unauthenticated
        return State.Verified.takeIf {
            currentUserRepository.flowOnIsUserVerified().firstOrNull() == true
        } ?: State.Unverified
    }

    sealed class State {
        data object Unauthenticated : State()
        data object Unverified : State()
        data object Verified : State()
    }
}
