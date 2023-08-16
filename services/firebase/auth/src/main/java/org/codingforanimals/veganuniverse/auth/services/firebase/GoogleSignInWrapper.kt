package org.codingforanimals.veganuniverse.auth.services.firebase

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleSignInWrapper(
    context: Context,
) {
    private val config = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.google_auth_id))
        .requestEmail()
        .build()

    val client = GoogleSignIn.getClient(context, config)
}