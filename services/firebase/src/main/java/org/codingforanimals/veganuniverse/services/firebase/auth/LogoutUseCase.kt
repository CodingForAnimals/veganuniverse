package org.codingforanimals.veganuniverse.services.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class LogoutUseCase(
    googleSignInWrapper: GoogleSignInWrapper,
    private val firebaseAuth: FirebaseAuth,
) {
    private val googleSignInClient = googleSignInWrapper.client

    private val FirebaseAuth.userIsProvidedByGoogle
        get() = currentUser?.providerData?.map { it.providerId }
            ?.contains(GoogleAuthProvider.PROVIDER_ID) == true

    suspend operator fun invoke() {
        if (firebaseAuth.userIsProvidedByGoogle) {
            googleSignInClient.signOut().await()
        }
        firebaseAuth.signOut()
    }
}