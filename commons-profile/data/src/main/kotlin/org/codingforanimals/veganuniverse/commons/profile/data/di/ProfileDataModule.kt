package org.codingforanimals.veganuniverse.commons.profile.data.di

import org.codingforanimals.veganuniverse.commons.profile.data.remote.profileRemoteModule
import org.codingforanimals.veganuniverse.commons.profile.data.storage.profileStorageModule
import org.koin.dsl.module

val profileDataModule = module {
    includes(
        profileStorageModule,
        profileRemoteModule,
    )
}
