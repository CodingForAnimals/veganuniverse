package org.codingforanimals.veganuniverse.user.auth

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.user.model.LoginResponse
import org.codingforanimals.veganuniverse.user.model.RegistrationResponse
import org.codingforanimals.veganuniverse.user.model.toRegistrationException
import org.codingforanimals.veganuniverse.user.model.toUser

internal class EmailAndPasswordAuthenticatorImpl(
    private val firebaseAuth: FirebaseAuth,
) : EmailAndPasswordAuthenticator {

    override suspend fun login(email: String, password: String): LoginResponse = try {
        val res = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        LoginResponse.Success(res.user!!.toUser())
    } catch (e: Throwable) {
        when (e) {
            is FirebaseAuthInvalidUserException -> LoginResponse.Exception.UserNotFound
            is FirebaseAuthInvalidCredentialsException -> LoginResponse.Exception.InvalidPassword
            is FirebaseNetworkException -> LoginResponse.Exception.ConnectionError
            else -> LoginResponse.Exception.UnknownException
        }
    }

    override suspend fun register(email: String, password: String): RegistrationResponse = try {
        val res = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        RegistrationResponse.Success(res.user!!.toUser())
    } catch (e: FirebaseException) {
        e.toRegistrationException()
    }
}