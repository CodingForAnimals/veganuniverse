package org.codingforanimals.veganuniverse.registration.presentation.di

import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.di.emailRegistrationModule
import org.codingforanimals.veganuniverse.registration.presentation.prompt.di.promptModule
import org.codingforanimals.veganuniverse.registration.presentation.signin.di.signInModule
import org.koin.dsl.module

val registrationModule = module {
    includes(
        promptModule,
        emailRegistrationModule,
        signInModule,
    )
}