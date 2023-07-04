package org.codingforanimals.veganuniverse.user.auth

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.user.model.RegistrationResponse
import org.codingforanimals.veganuniverse.user.model.toRegistrationException
import org.codingforanimals.veganuniverse.user.model.toUser

internal class GmailAuthUseCase(
    googleSignInWrapper: GoogleSignInWrapper,
    private val firebaseAuth: FirebaseAuth,
) {

    private val googleSignInClient = googleSignInWrapper.client
    val intent = googleSignInClient.signInIntent

    suspend operator fun invoke(intent: Intent): RegistrationResponse = try {
        val account = GoogleSignIn
            .getSignedInAccountFromIntent(intent)
            .getResult(ApiException::class.java)
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        val result = firebaseAuth.signInWithCredential(credentials).await()
        RegistrationResponse.Success(result.user!!.toUser())
    } catch (e: FirebaseException) {
        e.toRegistrationException()
    }
}
