package org.codingforanimals.veganuniverse.create.presentation.place.usecase

import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.auth.model.SendVerificationEmailResult

class SendVerificationEmailUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): SendVerificationEmailResult {
        return userRepository.sendUserVerificationEmail()
    }
}