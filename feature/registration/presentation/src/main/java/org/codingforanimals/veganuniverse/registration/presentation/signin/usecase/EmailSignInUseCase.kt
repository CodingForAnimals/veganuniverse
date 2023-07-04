package org.codingforanimals.veganuniverse.registration.presentation.signin.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.registration.presentation.model.EmailSignInStatus
import org.codingforanimals.veganuniverse.registration.presentation.model.toEmailSignInException
import org.codingforanimals.veganuniverse.user.UserRepository
import org.codingforanimals.veganuniverse.user.model.LoginResponse

class EmailSignInUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val userRepository: UserRepository,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    suspend operator fun invoke(email: String, password: String): Flow<EmailSignInStatus> = flow {
        emit(EmailSignInStatus.Loading)
        val response = withContext(ioDispatcher) {
            userRepository.loginWithEmailAndPassword(email, password)
        }
        val status = when (response) {
            is LoginResponse.Exception -> response.toEmailSignInException()
            is LoginResponse.Success -> EmailSignInStatus.Success
        }
        emit(status)
    }
}
