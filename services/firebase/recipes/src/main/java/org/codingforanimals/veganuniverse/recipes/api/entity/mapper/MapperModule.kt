package org.codingforanimals.veganuniverse.recipes.api.entity.mapper

import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.recipes.api.entity.RecipeFirebaseEntity
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val mapperModule = module {
    factory<OneWayEntityMapper<RecipeFirebaseEntity, Recipe>>(
        named(
            RecipeEntityMapper::class.java.simpleName
        )
    ) {
        RecipeEntityMapper(
            storageBucketWrapper = get()
        )
    }
}