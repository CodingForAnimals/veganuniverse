package org.codingforanimals.veganuniverse.create.recipe.domain.di

import org.codingforanimals.veganuniverse.create.recipe.domain.usecase.SubmitRecipe
import org.codingforanimals.veganuniverse.commons.profile.domain.di.PROFILE_RECIPE_USE_CASES
import org.codingforanimals.veganuniverse.commons.profile.domain.di.profileDomainModule
import org.codingforanimals.veganuniverse.commons.recipe.domain.di.recipeDomainModule
import org.codingforanimals.veganuniverse.commons.user.domain.di.userCommonDomainModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val createRecipeDomainModule = module {
    includes(
        profileDomainModule,
        recipeDomainModule,
        userCommonDomainModule,
    )

    factory {
        SubmitRecipe(
            recipeRepository = get(),
            profileContentUseCases = get(named(PROFILE_RECIPE_USE_CASES)),
            flowOnCurrentUser = get(),
        )
    }
}