package org.codingforanimals.veganuniverse.recipes.api.di

import org.codingforanimals.veganuniverse.recipes.api.DocumentSnapshotCache
import org.codingforanimals.veganuniverse.recipes.api.DocumentSnapshotLruCache
import org.codingforanimals.veganuniverse.recipes.api.RecipesApi
import org.codingforanimals.veganuniverse.recipes.api.RecipesFirebaseApi
import org.codingforanimals.veganuniverse.recipes.api.entity.mapper.RecipeEntityMapper
import org.codingforanimals.veganuniverse.recipes.api.entity.mapper.mapperModule
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val recipesFirebaseServiceModule = module {
    includes(
        firebaseServiceModule,
        mapperModule,
    )
    factory<RecipesApi> {
        RecipesFirebaseApi(
            firestore = get(),
            database = get(),
            storage = get(),
            recipeMapper = get(named(RecipeEntityMapper::class.java.simpleName)),
            recipeCache = get(),
        )
    }
    singleOf(::DocumentSnapshotLruCache) bind DocumentSnapshotCache::class
}