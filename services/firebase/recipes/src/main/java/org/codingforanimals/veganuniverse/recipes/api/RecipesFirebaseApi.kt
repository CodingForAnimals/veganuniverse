package org.codingforanimals.veganuniverse.recipes.api

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.recipes.api.entity.RecipeFirebaseEntity
import org.codingforanimals.veganuniverse.recipes.entity.RecipeForm
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection

internal class RecipesFirebaseApi(
    private val firestore: FirebaseFirestore,
    private val database: FirebaseDatabase,
) : RecipesApi {

    override suspend fun submitRecipe(recipeForm: RecipeForm) {
        val documentReference = firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .document()
        documentReference
            .set(recipeForm.toFirebaseEntity())
            .await()
        database
            .getReference("content/recipes/userRecipes/${recipeForm.userId}")
            .child(documentReference.id)
            .setValue(true)
    }

    private fun RecipeForm.toFirebaseEntity(): RecipeFirebaseEntity {
        return RecipeFirebaseEntity(
            userId = userId,
            title = title,
            category = category,
            description = description,
            likes = likes,
            ingredients = ingredients,
            tags = tags,
            steps = steps,
            prepTime = prepTime,
        )
    }
}