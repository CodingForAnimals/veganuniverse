package org.codingforanimals.veganuniverse.recipes.domain.di

import org.codingforanimals.veganuniverse.commons.profile.domain.di.PROFILE_RECIPE_USE_CASES
import org.codingforanimals.veganuniverse.recipes.domain.usecase.RecipeDetailsUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.di.userCommonDomainModule
import org.codingforanimals.veganuniverse.recipes.domain.usecase.EditRecipe
import org.codingforanimals.veganuniverse.recipes.domain.usecase.QueryRecipesById
import org.codingforanimals.veganuniverse.recipes.domain.usecase.ReportRecipe
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val recipesDomainModule = module {
    includes(
        userCommonDomainModule,
    )

    factoryOf(::ReportRecipe)
    factoryOf(::EditRecipe)
    factoryOf(::QueryRecipesById)

    factory {
        RecipeDetailsUseCases(
            reportRecipe = get(),
            editRecipe = get(),
            flowOnCurrentUser = get(),
            recipeRepository = get(),
            profileRecipeUseCases = get(named(PROFILE_RECIPE_USE_CASES))
        )
    }
}