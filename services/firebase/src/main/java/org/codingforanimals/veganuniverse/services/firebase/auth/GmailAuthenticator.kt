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
import org.codingforanimals.veganuniverse.services.firebase.auth.model.ProviderAuthenticationResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.toDto

private const val TAG = "GmailAuthenticator"

class GmailAuthenticator(
    googleSignInWrapper: GoogleSignInWrapper,
    private val firebaseAuth: FirebaseAuth,
) {

    private val googleSignInClient = googleSignInWrapper.client
    val intent = googleSignInClient.signInIntent

    suspend operator fun invoke(intent: Intent): ProviderAuthenticationResponse {
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
}
