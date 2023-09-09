package org.codingforanimals.veganuniverse.registration.presentation.emailsignin.viewmodel

sealed class EmailSignInScreenItem {
    data object Title : EmailSignInScreenItem()
    data object Email : EmailSignInScreenItem()
    data object Password : EmailSignInScreenItem()
    data object SignInButton : EmailSignInScreenItem()
}
