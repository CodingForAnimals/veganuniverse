package org.codingforanimals.veganuniverse.recipes.services.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.services.FetchRecipeService
import org.codingforanimals.veganuniverse.recipes.services.entity.RecipeFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection

private const val TAG = "FetchRecipeFirebaseServ"

internal class FetchRecipeFirebaseService(
    private val firestore: FirebaseFirestore,
    private val recipeMapper: OneWayEntityMapper<RecipeFirebaseEntity, Recipe>,
) : FetchRecipeService {
    override suspend fun invoke(id: String): Recipe? {
        val docSnap = firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .document(id)
            .get().await()
        return try {
            recipeMapper.map(docSnap.toObject(RecipeFirebaseEntity::class.java)!!)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }

}