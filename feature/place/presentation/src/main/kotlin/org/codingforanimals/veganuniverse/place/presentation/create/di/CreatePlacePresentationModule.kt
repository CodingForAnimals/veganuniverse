package org.codingforanimals.veganuniverse.place.presentation.create.di

import org.codingforanimals.veganuniverse.commons.user.presentation.di.userCommonPresentationModule
import org.codingforanimals.veganuniverse.place.presentation.create.CreatePlaceViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createPlacePresentationModule = module {
    includes(
        userCommonPresentationModule,
    )
    viewModelOf(::CreatePlaceViewModel)
}