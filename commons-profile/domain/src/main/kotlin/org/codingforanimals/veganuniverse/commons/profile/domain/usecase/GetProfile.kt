package org.codingforanimals.veganuniverse.commons.profile.domain.usecase

import org.codingforanimals.veganuniverse.commons.profile.domain.model.Profile
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository

class GetProfile(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Profile {
        return profileRepository.getProfile()
    }
}
