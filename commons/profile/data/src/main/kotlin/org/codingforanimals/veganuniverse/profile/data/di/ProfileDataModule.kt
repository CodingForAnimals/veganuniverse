package org.codingforanimals.veganuniverse.profile.data.di

import org.codingforanimals.veganuniverse.profile.data.remote.profileRemoteModule
import org.codingforanimals.veganuniverse.profile.data.storage.profileStorageModule
import org.koin.dsl.module

val profileDataModule = module {
    includes(
        profileStorageModule,
        profileRemoteModule,
    )
}
