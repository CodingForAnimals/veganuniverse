package org.codingforanimals.veganuniverse.registration.presentation.di

import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.di.emailRegistrationModule
import org.codingforanimals.veganuniverse.registration.presentation.emailsignin.di.signInModule
import org.codingforanimals.veganuniverse.registration.presentation.prompt.di.promptModule
import org.codingforanimals.veganuniverse.commons.user.domain.di.userCommonDomainModule
import org.codingforanimals.veganuniverse.registration.presentation.reauthentication.reauthenticationModule
import org.koin.dsl.module

val registrationModule = module {
    includes(
        promptModule,
        emailRegistrationModule,
        signInModule,
        userCommonDomainModule,
        reauthenticationModule,
    )
}