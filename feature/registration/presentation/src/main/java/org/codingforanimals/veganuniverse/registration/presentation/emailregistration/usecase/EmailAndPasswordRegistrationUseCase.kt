package org.codingforanimals.veganuniverse.registration.presentation.emailregistration.usecase

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.registration.presentation.usecase.UserAuthStatus

class EmailAndPasswordRegistrationUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()
    private val auth = Firebase.auth

    suspend operator fun invoke(email: String, password: String): Flow<UserAuthStatus> =
        flow {
            emit(UserAuthStatus.Loading)
            val result = try {
                val response = withContext(ioDispatcher) {
                    auth.createUserWithEmailAndPassword(email, password).await()
                }
                if (response.user == null) {
                    UserAuthStatus.Exception.UnknownFailure
                } else {
                    response.user?.sendEmailVerification()?.await()
                    UserAuthStatus.Success
                }
            } catch (e: Throwable) {
                when (e) {
                    is FirebaseAuthUserCollisionException -> UserAuthStatus.Exception.UserAlreadyExists
                    is FirebaseAuthInvalidCredentialsException -> UserAuthStatus.Exception.InvalidCredentials
                    else -> UserAuthStatus.Exception.UnknownFailure
                }
            }
            emit(result)
        }
}