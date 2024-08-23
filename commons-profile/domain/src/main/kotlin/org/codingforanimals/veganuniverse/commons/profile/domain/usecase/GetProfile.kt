package org.codingforanimals.veganuniverse.commons.profile.domain.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.profile.domain.model.Profile
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository

class GetProfile(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Profile? {
        return runCatching { profileRepository.getProfile() }.onFailure {
            Log.e("GetProfile", "Error getting profile", it)
            Analytics.logNonFatalException(it)
        }.getOrNull()
    }
}
