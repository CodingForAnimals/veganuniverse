package org.codingforanimals.veganuniverse.commons.recipe.data.remote.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class RecipeFirestoreEntity(
    @DocumentId val id: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val lowercaseName: String? = null,
    val description: String? = null,
    val tags: List<String>? = null,
    val ingredients: List<String>? = null,
    val steps: List<String>? = null,
    val prepTime: String? = null,
    val servings: String? = null,
    val imageId: String? = null,
    val likes: Int = 0,
    val validated: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
)
