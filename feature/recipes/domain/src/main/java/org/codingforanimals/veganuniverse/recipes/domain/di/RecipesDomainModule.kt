package org.codingforanimals.veganuniverse.recipes.domain.di

import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.domain.impl.RecipesRepositoryImpl
import org.codingforanimals.veganuniverse.recipes.services.firebase.di.recipesFirebaseServiceModule
import org.codingforanimals.veganuniverse.recipes.storage.di.recipesStorageModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipesDomainModule = module {
    includes(
        recipesFirebaseServiceModule,
        recipesStorageModule,
    )
    factoryOf(::RecipesRepositoryImpl) bind RecipesRepository::class
}