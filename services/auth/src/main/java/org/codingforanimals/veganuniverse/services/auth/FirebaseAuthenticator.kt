package org.codingforanimals.veganuniverse.services.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
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

    override suspend fun gmailReauthentication(context: Context) {
        val account = GoogleSignIn.getLastSignedInAccount(context)!!
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.currentUser!!.reauthenticate(credentials).await()
    }

    override suspend fun emailAuthentication(email: String, password: String) {
        val a = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        Log.e("pepe",a.user!!.toString())
    }

    override suspend fun emailReauthentication(email: String, password: String) {
        val credentials = EmailAuthProvider.getCredential(email, password)
        firebaseAuth.currentUser?.reauthenticate(credentials)!!.await()
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

    private val FirebaseAuth.userIsProvidedByGoogle
        get() = currentUser?.providerData?.map { it.providerId }
            ?.contains(GoogleAuthProvider.PROVIDER_ID) == true

    companion object {
        private const val TAG = "FirebaseAuthenticator"
        private const val defaultUserName ="Usuario de Universo Vegano"
    }
}