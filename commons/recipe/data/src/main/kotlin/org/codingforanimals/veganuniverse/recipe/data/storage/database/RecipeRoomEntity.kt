package org.codingforanimals.veganuniverse.recipe.data.storage.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
internal data class RecipeRoomEntity(
    @PrimaryKey val id: String,
    val userId: String?,
    val username: String?,
    val title: String,
    val description: String,
    val likes: Int,
    val createdAt: Date?,
    val tags: List<String>,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTime: String,
    val servings: String,
    val imageId: String,
)
