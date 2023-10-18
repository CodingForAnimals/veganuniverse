package org.codingforanimals.veganuniverse.recipes.api.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class RecipeFirebaseEntity(
    @DocumentId val id: String = "",
    val userId: String = "",
    val username: String = "",
    val title: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val prepTime: String = "",
    val servings: String = "",
    val likes: Int = 0,
    @ServerTimestamp val createdAt: Timestamp? = null,
)