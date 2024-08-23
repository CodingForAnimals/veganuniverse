package org.codingforanimals.veganuniverse.commons.profile.domain.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository

class DownloadAndStoreProfile(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() {
        runCatching {
            profileRepository.downloadAndStoreProfile()
        }.onFailure {
            Log.e("DownloadProfile", it.stackTraceToString())
            Analytics.logNonFatalException(it)
        }
    }
}