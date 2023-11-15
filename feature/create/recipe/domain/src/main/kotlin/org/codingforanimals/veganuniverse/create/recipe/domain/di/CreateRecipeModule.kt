package org.codingforanimals.veganuniverse.create.recipe.domain.di

import org.codingforanimals.veganuniverse.create.recipe.domain.RecipeCreator
import org.codingforanimals.veganuniverse.create.recipe.domain.RecipeCreatorImpl
import org.codingforanimals.veganuniverse.recipes.services.firebase.di.recipesFirebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val createRecipeDomainModule = module {
    includes(recipesFirebaseServiceModule)
    factoryOf(::RecipeCreatorImpl) bind RecipeCreator::class
}