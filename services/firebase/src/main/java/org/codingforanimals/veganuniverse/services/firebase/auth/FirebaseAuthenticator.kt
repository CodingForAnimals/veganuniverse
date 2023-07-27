package org.codingforanimals.veganuniverse.services.firebase.auth

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailRegistrationResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.ProviderAuthenticationResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.toDto

private const val TAG = "FirebaseAuthenticator"

class FirebaseAuthenticator(
    googleSignInWrapper: GoogleSignInWrapper,
    private val firebaseAuth: FirebaseAuth,
) : Authenticator {

    private val googleSignInClient = googleSignInWrapper.client
    override val googleSignInIntent = googleSignInClient.signInIntent

    override suspend fun emailLogin(email: String, password: String): EmailLoginResponse {
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

    override suspend fun emailRegistration(
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

    override suspend fun gmailAuthentication(intent: Intent): ProviderAuthenticationResponse {
        return try {
            val account = GoogleSignIn
                .getSignedInAccountFromIntent(intent)
                .getResult(ApiException::class.java)
            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = firebaseAuth.signInWithCredential(credentials).await()
            ProviderAuthenticationResponse.Success(result.user!!.toDto())
        } catch (e: FirebaseException) {
            Log.e(TAG, e.stackTraceToString())
            when (e) {
                is FirebaseAuthUserCollisionException -> ProviderAuthenticationResponse.Exception.UserAlreadyExists
                is FirebaseAuthInvalidCredentialsException -> ProviderAuthenticationResponse.Exception.InvalidCredentials
                is FirebaseNetworkException -> ProviderAuthenticationResponse.Exception.ConnectionError
                is FirebaseAuthInvalidUserException -> ProviderAuthenticationResponse.Exception.InvalidUser
                else -> ProviderAuthenticationResponse.Exception.UnknownFailure

            }
        }
    }

    override suspend fun logout() {
        if (firebaseAuth.userIsProvidedByGoogle) {
            googleSignInClient.signOut().await()
        }
        firebaseAuth.signOut()
    }

    private val FirebaseAuth.userIsProvidedByGoogle
        get() = currentUser?.providerData?.map { it.providerId }
            ?.contains(GoogleAuthProvider.PROVIDER_ID) == true

}