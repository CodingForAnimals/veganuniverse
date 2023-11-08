package org.codingforanimals.veganuniverse.user.services.firebase.impl

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.user.services.firebase.Authenticator
import org.codingforanimals.veganuniverse.user.services.firebase.config.GoogleSignInWrapper
import org.codingforanimals.veganuniverse.user.services.firebase.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.EmailRegistrationResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.ProviderAuthenticationResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.SendVerificationEmailResult
import org.codingforanimals.veganuniverse.user.services.firebase.model.UserFirebaseEntity

private const val TAG = "FirebaseAuthenticator"

class FirebaseAuthenticator(
    googleSignInWrapper: GoogleSignInWrapper,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseUserEntityMapper: OneWayEntityMapper<FirebaseUser, UserFirebaseEntity>,
) : Authenticator {

    private val googleSignInClient = googleSignInWrapper.client
    override val googleSignInIntent = googleSignInClient.signInIntent

    private val FirebaseAuth.userIsProvidedByGoogle
        get() = currentUser?.providerData?.map { it.providerId }
            ?.contains(GoogleAuthProvider.PROVIDER_ID) == true

    override val userFlow = callbackFlow {
        trySendBlocking(firebaseAuth.currentUser?.let { firebaseUserEntityMapper.map(it) })
        val authListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser?.let { firebaseUserEntityMapper.map(it) }
            trySendBlocking(user)
        }
        firebaseAuth.addAuthStateListener(authListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authListener)
        }
    }

    override suspend fun emailLogin(email: String, password: String): EmailLoginResponse {
        return try {
            val res = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            EmailLoginResponse.Success(firebaseUserEntityMapper.map(res.user!!))
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

    override suspend fun reauthenticateUser(): UserFirebaseEntity? {
        return try {
            firebaseAuth.currentUser?.reload()?.await()
            firebaseAuth.currentUser?.let { firebaseUserEntityMapper.map(it) }
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }

    override suspend fun emailRegistration(
        email: String,
        password: String,
        name: String,
    ): EmailRegistrationResponse {
        return try {
            val res = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = res.user!!
            val updateName = user.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            ).asDeferred()
            val sendEmailVerification = user.sendEmailVerification().asDeferred()

            awaitAll(updateName, sendEmailVerification)

            EmailRegistrationResponse.Success(firebaseUserEntityMapper.map(res.user!!))
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
            val account = GoogleSignIn.getSignedInAccountFromIntent(intent)
                .getResult(ApiException::class.java)
            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = firebaseAuth.signInWithCredential(credentials).await()
            ProviderAuthenticationResponse.Success(firebaseUserEntityMapper.map(result.user!!))
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

    override fun getCurrentUser(): UserFirebaseEntity? {
        return firebaseAuth.currentUser?.let { firebaseUserEntityMapper.map(it) }
    }


    override suspend fun sendUserVerificationEmail(): SendVerificationEmailResult {
        return try {
            firebaseAuth.currentUser?.sendEmailVerification()?.await()
            SendVerificationEmailResult.Success
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            when (e) {
                is FirebaseTooManyRequestsException -> SendVerificationEmailResult.TooManyRequests
                else -> SendVerificationEmailResult.UnknownError
            }
        }
    }
}