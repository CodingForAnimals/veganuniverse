package org.codingforanimals.veganuniverse.product.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.auth.model.SendVerificationEmailResult
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.product.data.model.Suggestion
import org.codingforanimals.veganuniverse.product.data.source.SuggestionRepository

private const val TAG = "SendSuggestion"

class ProductSuggestionUseCases(
    private val getUserStatus: GetUserStatus,
    private val suggestionRepository: SuggestionRepository,
    private val userRepository: UserRepository,
) {
    suspend fun sendReport(message: String): Result {
        val user = getUserStatus().firstOrNull() ?: return Result.GuestUser
        if (!user.isEmailVerified) return Result.UnverifiedEmail
        val report = Suggestion.Report(
            userId = user.id,
            userMessage = message,
        )
        return try {
            Result.Success(suggestionRepository.sendSuggestion(report))
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Result.Error
        }
    }

    suspend fun sendEdit(message: String): Result {
        val user = getUserStatus().firstOrNull() ?: return Result.GuestUser
        if (!user.isEmailVerified) return Result.UnverifiedEmail
        val report = Suggestion.Edit(
            userId = user.id,
            userMessage = message,
        )
        return try {
            Result.Success(suggestionRepository.sendSuggestion(report))
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Result.Error
        }
    }

    suspend fun sendVerificationEmail(): Boolean {
        return when (userRepository.sendUserVerificationEmail()) {
            SendVerificationEmailResult.Success -> true
            else -> false
        }
    }

    sealed class Result {
        data object Error : Result()
        data object GuestUser : Result()
        data object UnverifiedEmail : Result()
        data class Success(val reportId: String) : Result()
    }
}