package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator

interface VerifiedOnlyUserAction {
    suspend operator fun invoke(action: suspend () -> Unit)
}

internal class VerifiedOnlyUserActionImpl(
    private val getUserVerificationState: GetUserVerificationState,
    private val deeplinkNavigator: DeeplinkNavigator,
) : VerifiedOnlyUserAction {
    override suspend operator fun invoke(action: suspend () -> Unit) {
        when (getUserVerificationState()) {
            GetUserVerificationState.State.Verified -> action()
            GetUserVerificationState.State.Unauthenticated -> {
                deeplinkNavigator.navigate(DeepLink.AuthPrompt)
            }
            GetUserVerificationState.State.Unverified -> {
                deeplinkNavigator.navigate(DeepLink.ValidateEmailPrompt)
            }
        }
    }
}
