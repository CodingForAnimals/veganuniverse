package org.codingforanimals.veganuniverse.recipes.services.impl

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.services.FetchRecipeService
import org.codingforanimals.veganuniverse.recipes.services.entity.RecipeFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection
import org.codingforanimals.veganuniverse.storage.firestore.DocumentSnapshotCache

private const val TAG = "FetchRecipeFirebaseServ"

internal class FetchRecipeFirebaseService(
    private val firestore: FirebaseFirestore,
    private val recipesCache: DocumentSnapshotCache,
    private val recipeMapper: OneWayEntityMapper<RecipeFirebaseEntity, Recipe>,
) : FetchRecipeService {
    override suspend fun byId(id: String): Recipe? {
        return (
            recipesCache.getRecipe(id) ?: getFromFirestore(id)?.also { recipesCache.putRecipe(it) }
            )
            ?.toRecipe()
    }

    override suspend fun byIds(ids: List<String>): List<Recipe> = coroutineScope {
        val deferreds = ids.map { id ->
            async { byId(id) }
        }
        val result = awaitAll(*deferreds.toTypedArray())
        result.mapNotNull { it }
    }

    private suspend fun getFromFirestore(id: String): DocumentSnapshot? {
        return firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .document(id)
            .get().await()
    }

    private fun DocumentSnapshot.toRecipe(): Recipe? {
        return try {
            toObject(RecipeFirebaseEntity::class.java)?.let { recipeMapper.map(it) }
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }
}
