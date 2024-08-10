package org.codingforanimals.veganuniverse.onboarding.domain

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun wasOnboardingDismissed(): Flow<Boolean>
    suspend fun setWasOnboardingDismissed(value: Boolean)
}
