package org.codingforanimals.veganuniverse.recipes.domain.di

import org.codingforanimals.veganuniverse.recipes.api.di.recipesFirebaseServiceModule
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipesDomainModule = module {
    includes(recipesFirebaseServiceModule)
    factoryOf(::RecipesRepositoryImpl) bind RecipesRepository::class
}