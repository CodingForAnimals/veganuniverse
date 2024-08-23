package org.codingforanimals.veganuniverse.services.auth

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

internal class FirebaseAuthenticator(
    private val googleSignInClient: GoogleSignInClient,
    private val firebaseAuth: FirebaseAuth,
) : Authenticator {

    override val googleSignInIntent: Intent
        get() = googleSignInClient.signInIntent

    override val isUserProvidedByGoogle: Boolean
        get() = firebaseAuth.userIsProvidedByGoogle

    override suspend fun gmailAuthentication(intent: Intent): GmailAuthResult {
        return runCatching {
            val account = GoogleSignIn.getSignedInAccountFromIntent(intent)
                .getResult(ApiException::class.java)
            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = firebaseAuth.signInWithCredential(credentials).await()
            return if (result.additionalUserInfo?.isNewUser == true) {
                val name = result.user!!.displayName ?: defaultUserName
                val email = result.user?.email!!
                GmailAuthResult.NewUser(email, name)
            } else {
                GmailAuthResult.Success
            }
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            GmailAuthResult.Error(it)
        }
    }

    override suspend fun emailAuthentication(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun emailRegistration(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .await().user!!.sendEmailVerification().await()
    }

    override suspend fun logout() {
        if (firebaseAuth.userIsProvidedByGoogle) {
            googleSignInClient.signOut().await()
        }
        firebaseAuth.signOut()
    }

    override suspend fun isUserVerified(): Boolean {
        var user = checkNotNull(firebaseAuth.currentUser) {
            "Unauthenticated user"
        }
        var isEmailVerified = user.isEmailVerified
        var isProvidedByGoogle = user.isProvidedByGoogle

        if (isEmailVerified || isProvidedByGoogle) {
            return true
        } else {
            user.reload().await()

            user = checkNotNull(firebaseAuth.currentUser) {
                "Attempted to reload user and is unauthenticated"
            }

            isEmailVerified = user.isEmailVerified
            isProvidedByGoogle = user.isProvidedByGoogle

            return isEmailVerified || isProvidedByGoogle
        }
    }

    private val FirebaseAuth.userIsProvidedByGoogle
        get() = currentUser?.providerData?.map { it.providerId }
            ?.contains(GoogleAuthProvider.PROVIDER_ID) == true

    private val FirebaseUser.isProvidedByGoogle: Boolean
        get() = providerData.map { it.providerId }.contains(GoogleAuthProvider.PROVIDER_ID)

    companion object {
        private const val TAG = "FirebaseAuthenticator"
        private const val defaultUserName = "Usuario de Universo Vegano"
    }
}