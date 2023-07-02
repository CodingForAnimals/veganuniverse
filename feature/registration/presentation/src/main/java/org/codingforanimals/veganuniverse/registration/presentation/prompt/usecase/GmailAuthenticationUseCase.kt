package org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.registration.presentation.prompt.gauthid
import org.codingforanimals.veganuniverse.registration.presentation.usecase.UserAuthStatus

class GmailAuthenticationUseCase(
    private val context: Context,
) {

    private val auth = Firebase.auth

    val intent: Intent
        get() {
            val config = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(gauthid)
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(context, config).signInIntent
        }

    suspend operator fun invoke(result: ActivityResult): Flow<UserAuthStatus> = flow {
        emit(UserAuthStatus.Loading)
        if (result.resultCode == Activity.RESULT_OK) {
            val resultStatus = try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    .getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credentials).await()
                UserAuthStatus.Success
            } catch (e: Throwable) {
                when (e) {
                    is FirebaseAuthInvalidUserException -> {
                        UserAuthStatus.Exception.InvalidUser
                    }
                    is FirebaseAuthInvalidCredentialsException -> { // expired credentials
                        UserAuthStatus.Exception.InvalidCredentials
                    }
                    is FirebaseAuthUserCollisionException -> {
                        UserAuthStatus.Exception.UserAlreadyExists
                    }
                    else -> UserAuthStatus.Exception.UnknownFailure
                }
            }
            emit(resultStatus)
        }
    }
}