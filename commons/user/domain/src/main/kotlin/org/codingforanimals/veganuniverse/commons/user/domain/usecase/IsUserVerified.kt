package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository

interface IsUserVerified {
    suspend operator fun invoke(): Boolean
}

internal class IsUserVerifiedImpl(
    private val currentUserRepository: CurrentUserRepository,
) : IsUserVerified {
    override suspend fun invoke(): Boolean {
        return currentUserRepository.flowOnIsUserVerified().firstOrNull() ?: false
    }
}
