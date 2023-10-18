package org.codingforanimals.veganuniverse.registration.presentation.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.registration.presentation.model.LogoutStatus

private const val TAG = "LogoutUseCase"

class LogoutUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): Flow<LogoutStatus> = flow {
        emit(LogoutStatus.Loading)
        val status = try {
            userRepository.logout()
            LogoutStatus.Success
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            LogoutStatus.Exception.UnknownException
        }
        emit(status)
    }
}