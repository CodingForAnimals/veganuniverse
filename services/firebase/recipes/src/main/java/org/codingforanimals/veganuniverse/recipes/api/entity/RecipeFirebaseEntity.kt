package org.codingforanimals.veganuniverse.recipes.api.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import org.codingforanimals.veganuniverse.recipes.entity.PrepTime
import org.codingforanimals.veganuniverse.recipes.entity.Step

data class RecipeFirebaseEntity(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val category: String = "",
    val description: String = "",
    val likes: Int = 0,
    val ingredients: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val steps: List<Step> = emptyList(),
    val prepTime: PrepTime? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
)