package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository

interface SendVerificationEmail {
    suspend operator fun invoke(): Result<Unit>
}

internal class SendVerificationEmailImpl(
    private val currentUserRepository: CurrentUserRepository,
) : SendVerificationEmail {
    override suspend fun invoke(): Result<Unit> {
        return runCatching {
            currentUserRepository.sendVerificationEmail()
        }.onFailure { Log.e(TAG, it.stackTraceToString()) }
    }

    companion object {
        private const val TAG = "SendVerificationEmail"
    }
}