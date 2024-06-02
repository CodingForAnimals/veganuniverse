package org.codingforanimals.veganuniverse.create.recipe.domain.di

import org.codingforanimals.veganuniverse.create.recipe.domain.RecipeCreator
import org.codingforanimals.veganuniverse.create.recipe.domain.RecipeCreatorImpl
import org.codingforanimals.veganuniverse.create.recipe.domain.usecase.SubmitRecipe
import org.codingforanimals.veganuniverse.network.di.networkModule
import org.codingforanimals.veganuniverse.profile.domain.di.PROFILE_RECIPE_USE_CASES
import org.codingforanimals.veganuniverse.profile.domain.di.profileDomainModule
import org.codingforanimals.veganuniverse.recipe.domain.di.recipeDomainModule
import org.codingforanimals.veganuniverse.recipes.services.firebase.di.recipesFirebaseServiceModule
import org.codingforanimals.veganuniverse.user.domain.di.userDomainModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val createRecipeDomainModule = module {
    includes(recipesFirebaseServiceModule)
    factoryOf(::RecipeCreatorImpl) bind RecipeCreator::class

    includes(
        profileDomainModule,
        recipeDomainModule,
        networkModule,
        userDomainModule,
    )

    factory {
        SubmitRecipe(
            recipeRepository = get(),
            profileContentUseCases = get(named(PROFILE_RECIPE_USE_CASES)),
            currentUserRepository = get(),
            networkUtils = get(),
        )
    }
}