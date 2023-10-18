package org.codingforanimals.veganuniverse.recipes.api.entity.mapper

import java.util.Date
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.recipes.api.entity.RecipeFirebaseEntity
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.services.firebase.StorageBucketWrapper
import org.codingforanimals.veganuniverse.services.firebase.StoragePath

internal class RecipeEntityMapper(
    storageBucketWrapper: StorageBucketWrapper,
) : OneWayEntityMapper<RecipeFirebaseEntity, Recipe> {
    private val storageBucket = storageBucketWrapper.storageBucket
    override fun map(obj: RecipeFirebaseEntity): Recipe {
        return with(obj) {
            Recipe(
                id = id,
                userId = userId,
                username = username,
                title = title,
                description = description,
                likes = likes,
                createdAt = createdAt?.toDate() ?: Date(),
                tags = tags,
                ingredients = ingredients,
                steps = steps,
                prepTime = prepTime,
                servings = servings,
                imageRef = StoragePath.getRecipePictureRef(storageBucket, id)
            )
        }
    }
}