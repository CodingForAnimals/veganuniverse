package org.codingforanimals.veganuniverse.registration.presentation.emailregistration.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.auth.model.RegistrationResponse
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.registration.presentation.model.RegistrationStatus
import org.codingforanimals.veganuniverse.registration.presentation.model.toUserAuthException

class EmailAndPasswordRegistrationUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val userRepository: UserRepository,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    suspend operator fun invoke(email: String, password: String): Flow<RegistrationStatus> = flow {
        emit(RegistrationStatus.Loading)
        val response = withContext(ioDispatcher) {
            userRepository.createUserWithEmailAndPassword(email, password)
        }
        val status = when (response) {
            is RegistrationResponse.Exception -> response.toUserAuthException()
            is RegistrationResponse.Success -> RegistrationStatus.Success
        }
        emit(status)
    }
}