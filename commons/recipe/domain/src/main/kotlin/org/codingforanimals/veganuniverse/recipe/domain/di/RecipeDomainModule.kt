package org.codingforanimals.veganuniverse.recipe.domain.di

import org.codingforanimals.veganuniverse.recipe.data.di.recipeDataModule
import org.codingforanimals.veganuniverse.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.recipe.domain.repository.RecipeRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipeDomainModule = module {
    includes(
        recipeDataModule,
    )

    factoryOf(::RecipeRepositoryImpl) bind RecipeRepository::class
}
