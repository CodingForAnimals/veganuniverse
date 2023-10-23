package org.codingforanimals.veganuniverse.recipes.services.di

import org.codingforanimals.veganuniverse.recipes.services.FetchRecipeService
import org.codingforanimals.veganuniverse.recipes.services.FetchRecipesService
import org.codingforanimals.veganuniverse.recipes.services.RecipeLikesService
import org.codingforanimals.veganuniverse.recipes.services.SubmitRecipeService
import org.codingforanimals.veganuniverse.recipes.services.entity.mapper.RECIPE_ENTITY_MAPPER
import org.codingforanimals.veganuniverse.recipes.services.entity.mapper.mapperModule
import org.codingforanimals.veganuniverse.recipes.services.impl.FetchRecipeFirebaseService
import org.codingforanimals.veganuniverse.recipes.services.impl.FetchRecipesFirebaseService
import org.codingforanimals.veganuniverse.recipes.services.impl.RecipeLikesFirebaseService
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
    factory<FetchRecipesService> {
        FetchRecipesFirebaseService(
            firestore = get(),
            recipeMapper = get(named(RECIPE_ENTITY_MAPPER)),
            recipeCache = get(),
        )
    }

    factoryOf(::SubmitRecipeFirebaseService) bind SubmitRecipeService::class

    factoryOf(::RecipeLikesFirebaseService) bind RecipeLikesService::class

    factory<FetchRecipeService> {
        FetchRecipeFirebaseService(
            firestore = get(),
            recipeMapper = get(named(RECIPE_ENTITY_MAPPER))
        )
    }
}