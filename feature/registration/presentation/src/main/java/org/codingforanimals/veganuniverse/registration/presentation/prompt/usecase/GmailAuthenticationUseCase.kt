package org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.auth.model.RegistrationResponse
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.registration.presentation.model.RegistrationStatus
import org.codingforanimals.veganuniverse.registration.presentation.model.toUserAuthException

class GmailAuthenticationUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val userRepository: UserRepository,
) {
    private val ioDispatcher = coroutineDispatcherProvider.io()
    val intent = userRepository.googleSignInIntent

    suspend operator fun invoke(intent: Intent): Flow<RegistrationStatus> = flow {
        emit(RegistrationStatus.Loading)
        val response = withContext(ioDispatcher) {
            userRepository.authenticateWithGmail(intent)
        }
        val status = when (response) {
            is RegistrationResponse.Exception -> response.toUserAuthException()
            is RegistrationResponse.Success -> RegistrationStatus.Success
        }
        emit(status)
    }
}