package org.codingforanimals.veganuniverse.registration.presentation.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.registration.presentation.model.LogoutStatus
import org.codingforanimals.veganuniverse.user.UserRepository

class LogoutUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): Flow<LogoutStatus> = flow {
        emit(LogoutStatus.Loading)
        val status = try {
            userRepository.logout()
            LogoutStatus.Success
        } catch (e: Throwable) {
            LogoutStatus.Exception.UnknownException
        }
        emit(status)
    }
}