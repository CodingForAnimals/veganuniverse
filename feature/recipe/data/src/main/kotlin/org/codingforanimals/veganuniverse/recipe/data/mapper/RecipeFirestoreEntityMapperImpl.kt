package org.codingforanimals.veganuniverse.recipe.data.mapper

import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.firebase.storage.model.PublicImageApi
import org.codingforanimals.veganuniverse.recipe.data.source.RecipeFirestoreDataSource.Companion.BASE_RECIPE_PICTURE_PATH
import org.codingforanimals.veganuniverse.recipe.data.model.RecipeFirestoreEntity


internal class RecipeFirestoreEntityMapperImpl(
    private val publicImageApi: PublicImageApi,
) : RecipeFirestoreEntityMapper {
    override fun mapToDataModel(firestoreEntity: RecipeFirestoreEntity): Recipe =
        with(firestoreEntity) {
            Recipe(
                id = id,
                userId = userId,
                username = username,
                name = name.orEmpty(),
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
