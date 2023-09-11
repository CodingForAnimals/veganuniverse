package org.codingforanimals.veganuniverse.profile.presentation.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.auth.UserRepository

private const val TAG = "LogoutUseCase"

internal class LogoutUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<LogoutState> = flow {
        emit(LogoutState.Loading)
        try {
            userRepository.logout()
            emit(LogoutState.Success)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            emit(LogoutState.Error)
        }
    }
}

sealed class LogoutState {
    data object Loading : LogoutState()
    data object Success : LogoutState()
    data object Error : LogoutState()
}