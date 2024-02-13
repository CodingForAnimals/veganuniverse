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
import com.google.firebase.firestore.FirebaseFirestore
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
import org.codingforanimals.veganuniverse.user.services.firebase.entity.UserFirebaseEntity

private const val TAG = "FirebaseAuthenticator"

class FirebaseAuthenticator(
    firestore: FirebaseFirestore,
    googleSignInWrapper: GoogleSignInWrapper,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseUserEntityMapper: OneWayEntityMapper<FirebaseUser, UserFirebaseEntity>,
) : Authenticator {

    private val usersCollection = firestore.collection("users")

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
            val firebaseUser = res.user!!
            val updateName = firebaseUser.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            ).asDeferred()
            val sendEmailVerification = firebaseUser.sendEmailVerification().asDeferred()
            val userEntity = firebaseUserEntityMapper.map(firebaseUser)
            val userDocumentDeferred = usersCollection.document().set(userEntity).asDeferred()

            awaitAll(updateName, sendEmailVerification, userDocumentDeferred)

            EmailRegistrationResponse.Success(userEntity)
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
            val userEntity = firebaseUserEntityMapper.map(result.user!!)
            if (result.additionalUserInfo?.isNewUser == true) {
                usersCollection.document().set(userEntity).await()
            }
            ProviderAuthenticationResponse.Success(userEntity)
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