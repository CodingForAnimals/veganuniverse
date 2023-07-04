package org.codingforanimals.veganuniverse.user.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.user.model.LogoutResponse

internal class LogoutUseCase(
    googleSignInWrapper: GoogleSignInWrapper,
    private val firebaseAuth: FirebaseAuth,
) {
    private val googleSignInClient = googleSignInWrapper.client

    private val FirebaseAuth.userIsProvidedByGoogle
        get() = currentUser?.providerData?.map { it.providerId }
            ?.contains(GoogleAuthProvider.PROVIDER_ID) == true

    suspend operator fun invoke(): LogoutResponse {
        return try {
            if (firebaseAuth.userIsProvidedByGoogle) {
                googleSignInClient.signOut().await()
            }
            firebaseAuth.signOut()
            LogoutResponse.Success
        } catch (e: Throwable) {
            LogoutResponse.Exception
        }
    }
}