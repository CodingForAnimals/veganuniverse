package org.codingforanimals.veganuniverse.profile.di

import org.codingforanimals.veganuniverse.profile.home.presentation.di.profileHomePresentationModule
import org.codingforanimals.veganuniverse.profile.itemlist.presentation.di.profileItemListModule
import org.koin.dsl.module

val profileFeatureModule = module {
    includes(
        profileHomePresentationModule,
        profileItemListModule,
    )
}