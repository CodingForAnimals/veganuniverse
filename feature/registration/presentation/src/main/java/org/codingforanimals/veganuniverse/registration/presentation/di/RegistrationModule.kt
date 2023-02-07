package org.codingforanimals.registration.presentation.di

import org.codingforanimals.registration.presentation.RegistrationPresenter
import org.codingforanimals.registration.presentation.RegistrationPresenterImpl
import org.koin.dsl.module

val registrationModule = module {
    factory<RegistrationPresenter> { RegistrationPresenterImpl() }
}