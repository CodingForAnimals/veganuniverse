package org.codingforanimals.veganuniverse.recipes.domain.di

import org.codingforanimals.veganuniverse.profile.domain.di.PROFILE_RECIPE_USE_CASES
import org.codingforanimals.veganuniverse.recipes.domain.RecipesRepository
import org.codingforanimals.veganuniverse.recipes.domain.impl.RecipesRepositoryImpl
import org.codingforanimals.veganuniverse.recipes.domain.usecase.RecipeDetailsUseCases
import org.codingforanimals.veganuniverse.recipes.services.firebase.di.recipesFirebaseServiceModule
import org.codingforanimals.veganuniverse.recipes.storage.di.recipesStorageModule
import org.codingforanimals.veganuniverse.user.domain.di.userDomainModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val recipesDomainModule = module {
    includes(
        userDomainModule,
        recipesFirebaseServiceModule,
        recipesStorageModule,
    )
    factoryOf(::RecipesRepositoryImpl) bind RecipesRepository::class

    factory {
        RecipeDetailsUseCases(
            getCurrentUser = get(),
            recipeRepository = get(),
            profileRecipeUseCases = get(named(PROFILE_RECIPE_USE_CASES))
        )
    }
}