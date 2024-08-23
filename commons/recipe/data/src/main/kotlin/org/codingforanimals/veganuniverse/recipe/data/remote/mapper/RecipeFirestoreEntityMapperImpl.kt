package org.codingforanimals.veganuniverse.recipe.data.remote.mapper

import org.codingforanimals.veganuniverse.firebase.storage.model.PublicImageApi
import org.codingforanimals.veganuniverse.firebase.storage.model.StoragePath
import org.codingforanimals.veganuniverse.recipe.data.remote.RecipeFirestoreDataSource.Companion.BASE_RECIPE_PICTURE_PATH
import org.codingforanimals.veganuniverse.recipe.data.remote.entity.RecipeFirestoreEntity
import org.codingforanimals.veganuniverse.recipe.model.Recipe
import org.codingforanimals.veganuniverse.recipe.model.RecipeTag

internal class RecipeFirestoreEntityMapperImpl(
    private val publicImageApi: PublicImageApi,
) : RecipeFirestoreEntityMapper {
    override fun mapToDataModel(firestoreEntity: RecipeFirestoreEntity): Recipe =
        with(firestoreEntity) {
            Recipe(
                id = id,
                userId = userId,
                username = username,
                title = title.orEmpty(),
                description = description.orEmpty(),
                likes = likes,
                createdAt = createdAt?.toDate(),
                tags = tags?.mapNotNull { RecipeTag.fromString(it) } ?: emptyList(),
                ingredients = ingredients.orEmpty(),
                steps = steps.orEmpty(),
                prepTime = prepTime.orEmpty(),
                servings = servings.orEmpty(),
                imageUrl = imageId?.let {
                    publicImageApi.getFilePath(
                        path = BASE_RECIPE_PICTURE_PATH,
                        imageId = it,
                    )
                },
                validated = validated,
            )
        }
}
