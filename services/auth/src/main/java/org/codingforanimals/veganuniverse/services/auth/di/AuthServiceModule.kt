package org.codingforanimals.veganuniverse.services.auth.di

import com.google.firebase.auth.FirebaseAuth
import org.codingforanimals.veganuniverse.services.auth.Authenticator
import org.codingforanimals.veganuniverse.services.auth.FirebaseAuthenticator
import org.codingforanimals.veganuniverse.services.auth.GoogleSignInWrapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val authServiceModule = module {
    factoryOf(::GoogleSignInWrapper)
    factory<Authenticator> {
        FirebaseAuthenticator(
            googleSignInClient = get<GoogleSignInWrapper>().client,
            firebaseAuth = FirebaseAuth.getInstance(),
        )
    }
}
