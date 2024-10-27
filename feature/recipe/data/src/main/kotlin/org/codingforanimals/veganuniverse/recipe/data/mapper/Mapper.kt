package org.codingforanimals.veganuniverse.recipe.data.mapper

import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.recipe.data.model.RecipeFirestoreEntity


fun Recipe.toNewFirestoreEntity(imageId: String): RecipeFirestoreEntity {
    return RecipeFirestoreEntity(
        id = null,
        userId = userId,
        username = username,
        name = name,
        nameLowercase = name.lowercase(),
        description = description,
        tags = tags.map { it.name },
        ingredients = ingredients,
        steps = steps,
        prepTime = prepTime,
        servings = servings,
        imageId = imageId,
        validated = true,
        likes = 0,
        createdAt = null,
    )
}
