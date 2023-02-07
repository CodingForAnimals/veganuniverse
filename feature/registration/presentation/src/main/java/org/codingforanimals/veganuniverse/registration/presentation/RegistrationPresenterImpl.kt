package org.codingforanimals.registration.presentation

import kotlinx.coroutines.delay

internal class RegistrationPresenterImpl: RegistrationPresenter {
    override suspend fun isUserLoggedIn(): Boolean {
        delay(1000)
        return false
    }
}

interface RegistrationPresenter {
    suspend fun isUserLoggedIn(): Boolean
}