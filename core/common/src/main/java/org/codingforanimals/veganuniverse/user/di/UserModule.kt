package org.codingforanimals.veganuniverse.user.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.codingforanimals.veganuniverse.user.UserRepository
import org.codingforanimals.veganuniverse.user.UserRepositoryImpl
import org.codingforanimals.veganuniverse.user.auth.EmailAndPasswordAuthenticator
import org.codingforanimals.veganuniverse.user.auth.EmailAndPasswordAuthenticatorImpl
import org.codingforanimals.veganuniverse.user.auth.GmailAuthUseCase
import org.codingforanimals.veganuniverse.user.auth.GoogleSignInWrapper
import org.codingforanimals.veganuniverse.user.auth.LogoutUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val userModule = module {
    factory { Firebase.auth }
    factoryOf(::GoogleSignInWrapper)
    factoryOf(::GmailAuthUseCase)
    factoryOf(::LogoutUseCase)
    factory<EmailAndPasswordAuthenticator> { EmailAndPasswordAuthenticatorImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
}