package org.codingforanimals.veganuniverse.profile.domain

import org.codingforanimals.veganuniverse.profile.domain.model.UserFeatureContributions

interface ProfileRepository {
    suspend fun getUserFeatureContributions(userId: String): UserFeatureContributions
}