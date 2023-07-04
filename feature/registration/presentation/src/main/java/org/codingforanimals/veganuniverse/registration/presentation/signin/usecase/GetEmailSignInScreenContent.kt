package org.codingforanimals.veganuniverse.registration.presentation.signin.usecase

import org.codingforanimals.veganuniverse.registration.presentation.signin.viewmodel.EmailSignInScreenItem

class GetEmailSignInScreenContent {
    operator fun invoke() = listOf(
        EmailSignInScreenItem.Title,
        EmailSignInScreenItem.Email,
        EmailSignInScreenItem.Password,
        EmailSignInScreenItem.SignInButton,
    )
}