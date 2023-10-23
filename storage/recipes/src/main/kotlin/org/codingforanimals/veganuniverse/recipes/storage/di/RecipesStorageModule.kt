package org.codingforanimals.veganuniverse.recipes.storage.di

import org.codingforanimals.veganuniverse.recipes.storage.RecipeCache
import org.codingforanimals.veganuniverse.recipes.storage.RecipeListCache
import org.codingforanimals.veganuniverse.recipes.storage.RecipeListLruCache
import org.codingforanimals.veganuniverse.recipes.storage.RecipeLruCache
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipesStorageModule = module {
    singleOf(::RecipeListLruCache) bind RecipeListCache::class
    singleOf(::RecipeLruCache) bind RecipeCache::class
}