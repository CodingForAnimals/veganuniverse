package org.codingforanimals.veganuniverse.registration.presentation.emailsignin.usecase

import org.codingforanimals.veganuniverse.registration.presentation.emailsignin.viewmodel.EmailSignInScreenItem

class GetEmailSignInScreenContent {
    operator fun invoke() = listOf(
        EmailSignInScreenItem.Title,
        EmailSignInScreenItem.Email,
        EmailSignInScreenItem.Password,
        EmailSignInScreenItem.SignInButton,
    )
}