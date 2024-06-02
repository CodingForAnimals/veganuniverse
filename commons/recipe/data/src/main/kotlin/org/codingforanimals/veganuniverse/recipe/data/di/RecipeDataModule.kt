package org.codingforanimals.veganuniverse.recipe.data.di

import org.codingforanimals.veganuniverse.firebase.storage.di.firebaseStorageModule
import org.codingforanimals.veganuniverse.recipe.data.remote.di.recipeDataRemoteModule
import org.codingforanimals.veganuniverse.recipe.data.storage.di.recipeDataStorageModule
import org.koin.dsl.module

val recipeDataModule = module {
    includes(
        firebaseStorageModule,
        recipeDataRemoteModule,
        recipeDataStorageModule,
    )
}
