package org.codingforanimals.veganuniverse.commons.profile.domain.di

import org.codingforanimals.veganuniverse.commons.profile.data.di.profileDataModule
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepositoryImpl
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.GetProfile
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfilePlaceUseCases
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileProductUseCases
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileRecipeUseCases
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val profileDomainModule = module {
    includes(
        profileDataModule,
    )

    factoryOf(::ProfileRepositoryImpl) bind ProfileRepository::class

    factory<ProfileContentUseCases>(named(PROFILE_PLACE_USE_CASES)) {
        ProfilePlaceUseCases(get(), get())
    }

    factory<ProfileContentUseCases>(named(PROFILE_RECIPE_USE_CASES)) {
        ProfileRecipeUseCases(get(), get())
    }

    factory<ProfileContentUseCases>(named(PROFILE_PRODUCT_USE_CASES)) {
        ProfileProductUseCases(get(), get())
    }

    factoryOf(::GetProfile)
}

const val PROFILE_PLACE_USE_CASES = "profile-place-use-cases"
const val PROFILE_RECIPE_USE_CASES = "profile-recipe-use-cases"
const val PROFILE_PRODUCT_USE_CASES = "profile-product-use-cases"
