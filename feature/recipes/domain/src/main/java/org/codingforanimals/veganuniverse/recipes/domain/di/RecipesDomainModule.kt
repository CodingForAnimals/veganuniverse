package org.codingforanimals.veganuniverse.recipes.domain.di

import org.codingforanimals.veganuniverse.recipes.domain.RecipeCache
import org.codingforanimals.veganuniverse.recipes.domain.RecipeListCache
import org.codingforanimals.veganuniverse.recipes.domain.RecipeListLruCache
import org.codingforanimals.veganuniverse.recipes.domain.RecipeLruCache
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepositoryImpl
import org.codingforanimals.veganuniverse.recipes.services.di.recipesFirebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipesDomainModule = module {
    includes(recipesFirebaseServiceModule)
    factoryOf(::RecipesRepositoryImpl) bind RecipesRepository::class
    singleOf(::RecipeListLruCache) bind RecipeListCache::class
    singleOf(::RecipeLruCache) bind RecipeCache::class
}