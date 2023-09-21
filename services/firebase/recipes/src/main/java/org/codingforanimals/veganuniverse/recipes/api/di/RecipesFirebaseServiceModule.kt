package org.codingforanimals.veganuniverse.recipes.api.di

import org.codingforanimals.veganuniverse.recipes.api.RecipesApi
import org.codingforanimals.veganuniverse.recipes.api.RecipesFirebaseApi
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipesFirebaseServiceModule = module {
    factoryOf(::RecipesFirebaseApi) bind RecipesApi::class
}