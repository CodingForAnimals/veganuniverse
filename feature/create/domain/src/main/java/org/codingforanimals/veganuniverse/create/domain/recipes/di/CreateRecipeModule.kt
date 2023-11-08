package org.codingforanimals.veganuniverse.create.domain.recipes.di

import org.codingforanimals.veganuniverse.create.domain.recipes.RecipeCreator
import org.codingforanimals.veganuniverse.create.domain.recipes.RecipeCreatorImpl
import org.codingforanimals.veganuniverse.recipes.services.firebase.di.recipesFirebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val createRecipeModule = module {
    includes(recipesFirebaseServiceModule)
    factoryOf(::RecipeCreatorImpl) bind RecipeCreator::class
}