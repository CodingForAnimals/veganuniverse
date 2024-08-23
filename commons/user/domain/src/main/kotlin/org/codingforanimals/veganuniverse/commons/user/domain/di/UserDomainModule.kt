package org.codingforanimals.veganuniverse.commons.user.domain.di

import org.codingforanimals.veganuniverse.commons.user.data.di.userCommonDataModule
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentCurrentUserRepositoryImpl
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.AuthenticationUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUserImpl
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.GetUser
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.GetUserImpl
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.GetUserVerificationState
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.IsUserVerified
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.IsUserVerifiedImpl
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.SendVerificationEmail
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.SendVerificationEmailImpl
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.EvaluateUserEmailVerificationState
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.VerifiedOnlyUserAction
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.VerifiedOnlyUserActionImpl
import org.codingforanimals.veganuniverse.services.auth.di.authServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val userCommonDomainModule = module {
    includes(
        authServiceModule,
        userCommonDataModule,
    )
    factoryOf(::CurrentCurrentUserRepositoryImpl) bind CurrentUserRepository::class
    factoryOf(::FlowOnCurrentUserImpl) bind FlowOnCurrentUser::class
    factoryOf(::IsUserVerifiedImpl) bind IsUserVerified::class
    factoryOf(::GetUserImpl) bind GetUser::class
    factoryOf(::SendVerificationEmailImpl) bind SendVerificationEmail::class
    factoryOf(::AuthenticationUseCases)
    factoryOf(::GetUserVerificationState)
    factoryOf(::VerifiedOnlyUserActionImpl) bind VerifiedOnlyUserAction::class
    factoryOf(::EvaluateUserEmailVerificationState)
}
