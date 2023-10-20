package org.codingforanimals.veganuniverse.recipes.services.di

import org.codingforanimals.veganuniverse.recipes.services.DocumentSnapshotCache
import org.codingforanimals.veganuniverse.recipes.services.DocumentSnapshotLruCache
import org.codingforanimals.veganuniverse.recipes.services.RecipesFirebaseService
import org.codingforanimals.veganuniverse.recipes.services.RecipesService
import org.codingforanimals.veganuniverse.recipes.services.entity.mapper.RECIPE_ENTITY_MAPPER
import org.codingforanimals.veganuniverse.recipes.services.entity.mapper.mapperModule
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
    factory<RecipesService> {
        RecipesFirebaseService(
            firestore = get(),
            database = get(),
            storage = get(),
            recipeMapper = get(named(RECIPE_ENTITY_MAPPER)),
            recipeCache = get(),
        )
    }
    singleOf(::DocumentSnapshotLruCache) bind DocumentSnapshotCache::class
}