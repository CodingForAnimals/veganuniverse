package org.codingforanimals.veganuniverse.recipes.services.impl

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
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
    override suspend fun invoke(id: String): Recipe? {
        return recipesCache.getRecipe(id)?.let { docSnap ->
            mapDocSnapToRecipe(docSnap)
        } ?: run {
            val docSnap = firestore
                .collection(FirestoreCollection.Content.Recipes.ITEMS)
                .document(id)
                .get().await()
            mapDocSnapToRecipe(docSnap)
        }
    }

    private fun mapDocSnapToRecipe(documentSnapshot: DocumentSnapshot): Recipe? {
        return try {
            recipeMapper.map(documentSnapshot.toObject(RecipeFirebaseEntity::class.java)!!)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }

    override suspend fun invoke(id: List<String>): List<Recipe> {
        val docSnapArray = firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .whereIn(FieldPath.documentId(), id)
            .get().await()
        // esta vacia
        return docSnapArray.mapNotNull { mapDocSnapToRecipe(it) }
    }

}