package org.codingforanimals.veganuniverse.services.firebase.auth

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailRegistrationResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.toDto

private const val TAG = "EmailAuthenticator"

internal class FirebaseEmailAuthenticator(
    private val firebaseAuth: FirebaseAuth,
) : EmailAuthenticator {

    override suspend fun login(email: String, password: String): EmailLoginResponse {
        return try {
            val res = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            EmailLoginResponse.Success(res.user!!.toDto())
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            when (e) {
                is FirebaseAuthInvalidUserException -> EmailLoginResponse.Exception.UserNotFound
                is FirebaseAuthInvalidCredentialsException -> EmailLoginResponse.Exception.InvalidPassword
                is FirebaseNetworkException -> EmailLoginResponse.Exception.ConnectionError
                else -> EmailLoginResponse.Exception.UnknownException
            }
        }
    }

    override suspend fun register(
        email: String,
        password: String,
    ): EmailRegistrationResponse {
        return try {
            val res = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            EmailRegistrationResponse.Success(res.user!!.toDto())
        } catch (e: FirebaseException) {
            Log.e(TAG, e.stackTraceToString())
            when (e) {
                is FirebaseAuthUserCollisionException -> EmailRegistrationResponse.Exception.UserAlreadyExists
                is FirebaseAuthInvalidCredentialsException -> EmailRegistrationResponse.Exception.InvalidCredentials
                is FirebaseNetworkException -> EmailRegistrationResponse.Exception.ConnectionError
                is FirebaseAuthInvalidUserException -> EmailRegistrationResponse.Exception.InvalidUser
                else -> EmailRegistrationResponse.Exception.UnknownFailure
            }
        }
    }
}