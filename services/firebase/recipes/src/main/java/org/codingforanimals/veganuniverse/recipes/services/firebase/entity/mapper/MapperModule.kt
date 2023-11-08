package org.codingforanimals.veganuniverse.recipes.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.services.firebase.entity.RecipeFirebaseEntity
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val mapperModule = module {
    factory<OneWayEntityMapper<RecipeFirebaseEntity, Recipe>>(
        named(RECIPE_ENTITY_MAPPER)
    ) {
        RecipeEntityMapper(
            storageBucketWrapper = get()
        )
    }
}

internal const val RECIPE_ENTITY_MAPPER = "recipe-entity-mapper"