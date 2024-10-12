package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.user.domain.model.User

interface VerifiedOnlyUserAction {
    suspend operator fun invoke(action: suspend (User) -> Unit)
}

internal class VerifiedOnlyUserActionImpl(
    private val getUserVerificationState: GetUserVerificationState,
    private val deeplinkNavigator: DeeplinkNavigator,
) : VerifiedOnlyUserAction {
    override suspend operator fun invoke(action: suspend (User) -> Unit) {
        when (val state = getUserVerificationState()) {
            is GetUserVerificationState.State.Verified -> action(state.user)
            GetUserVerificationState.State.Unauthenticated -> {
                deeplinkNavigator.navigate(DeepLink.AuthPrompt)
            }
            is GetUserVerificationState.State.Unverified -> {
                deeplinkNavigator.navigate(DeepLink.ValidateEmailPrompt)
            }
        }
    }
}
