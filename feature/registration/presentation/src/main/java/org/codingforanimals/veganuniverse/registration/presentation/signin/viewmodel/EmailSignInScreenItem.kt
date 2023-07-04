package org.codingforanimals.veganuniverse.registration.presentation.signin.viewmodel

sealed class EmailSignInScreenItem {
    object Title : EmailSignInScreenItem()
    object Email : EmailSignInScreenItem()
    object Password : EmailSignInScreenItem()
    object SignInButton : EmailSignInScreenItem()
}
