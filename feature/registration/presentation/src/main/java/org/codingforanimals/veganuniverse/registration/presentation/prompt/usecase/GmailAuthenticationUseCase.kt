package org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.registration.presentation.model.RegistrationStatus
import org.codingforanimals.veganuniverse.registration.presentation.model.toUserAuthException
import org.codingforanimals.veganuniverse.user.UserRepository
import org.codingforanimals.veganuniverse.user.model.RegistrationResponse

class GmailAuthenticationUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val userRepository: UserRepository,
) {
    private val ioDispatcher = coroutineDispatcherProvider.io()
    val intent = userRepository.gmailAuthIntent

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