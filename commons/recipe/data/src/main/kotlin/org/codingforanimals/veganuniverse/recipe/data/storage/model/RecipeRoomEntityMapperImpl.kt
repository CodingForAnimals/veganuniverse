package org.codingforanimals.veganuniverse.recipe.data.storage.model

import org.codingforanimals.veganuniverse.firebase.storage.model.PublicImageApi
import org.codingforanimals.veganuniverse.recipe.data.storage.database.RecipeRoomEntity
import org.codingforanimals.veganuniverse.recipe.data.utils.RECIPES_IMAGE_STORAGE_PATH
import org.codingforanimals.veganuniverse.recipe.model.Recipe
import org.codingforanimals.veganuniverse.recipe.model.RecipeTag

internal class RecipeRoomEntityMapperImpl(
    private val publicImageApi: PublicImageApi,
) : RecipeRoomEntityMapper {
    override fun mapToEntity(model: Recipe): RecipeRoomEntity? = with(model) {
        return RecipeRoomEntity(
            id = id ?: return@with null,
            userId = userId,
            username = username,
            title = title,
            description = description,
            likes = likes,
            createdAt = createdAt,
            tags = tags.map { it.name },
            ingredients = ingredients,
            steps = steps,
            prepTime = prepTime,
            servings = servings,
            imageId = imageUrl?.split("/")?.lastOrNull() ?: "",
        )
    }

    override fun mapToModel(entity: RecipeRoomEntity): Recipe = with(entity) {
        return Recipe(
            id = id,
            userId = userId,
            username = username,
            title = title,
            description = description,
            likes = likes,
            createdAt = createdAt,
            tags = tags.mapNotNull { RecipeTag.fromString(it) },
            ingredients = ingredients,
            steps = steps,
            prepTime = prepTime,
            servings = servings,
            imageUrl = publicImageApi.getFilePath(RECIPES_IMAGE_STORAGE_PATH, imageId),
            validated = false,
        )
    }
}