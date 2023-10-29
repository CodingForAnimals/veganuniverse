package org.codingforanimals.veganuniverse.recipes.services.di

import org.codingforanimals.veganuniverse.recipes.services.FetchRecipeService
import org.codingforanimals.veganuniverse.recipes.services.RecipesQueryService
import org.codingforanimals.veganuniverse.recipes.services.SubmitRecipeService
import org.codingforanimals.veganuniverse.recipes.services.entity.mapper.RECIPE_ENTITY_MAPPER
import org.codingforanimals.veganuniverse.recipes.services.entity.mapper.mapperModule
import org.codingforanimals.veganuniverse.recipes.services.impl.FetchRecipeFirebaseService
import org.codingforanimals.veganuniverse.recipes.services.impl.RecipesFirebaseQueryService
import org.codingforanimals.veganuniverse.recipes.services.impl.SubmitRecipeFirebaseService
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.codingforanimals.veganuniverse.storage.firestore.di.firestoreStorageModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val recipesFirebaseServiceModule = module {
    includes(
        firestoreStorageModule,
        firebaseServiceModule,
        mapperModule,
    )
    factory<RecipesQueryService> {
        RecipesFirebaseQueryService(
            firestore = get(),
            recipeMapper = get(named(RECIPE_ENTITY_MAPPER)),
            recipeCache = get(),
        )
    }

    factoryOf(::SubmitRecipeFirebaseService) bind SubmitRecipeService::class

    factory<FetchRecipeService> {
        FetchRecipeFirebaseService(
            firestore = get(),
            recipeMapper = get(named(RECIPE_ENTITY_MAPPER)),
            recipesCache = get()
        )
    }
}