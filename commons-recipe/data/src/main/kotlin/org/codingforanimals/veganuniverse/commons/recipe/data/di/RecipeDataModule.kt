package org.codingforanimals.veganuniverse.commons.recipe.data.di

import org.codingforanimals.veganuniverse.firebase.storage.di.firebaseStorageModule
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.di.recipeDataRemoteModule
import org.koin.dsl.module

val recipeDataModule = module {
    includes(
        firebaseStorageModule,
        recipeDataRemoteModule,
    )
}
