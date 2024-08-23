package org.codingforanimals.veganuniverse.profile.di

import org.codingforanimals.veganuniverse.profile.itemlist.presentation.di.profileItemListModule
import org.koin.dsl.module

val profileFeatureModule = module {
    includes(
        profileItemListModule,
    )
}