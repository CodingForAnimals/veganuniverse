package org.codingforanimals.veganuniverse.commons.profile.domain.usecase

import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository

class DownloadAndStoreProfile(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() {
        profileRepository.downloadAndStoreProfile()
    }
}