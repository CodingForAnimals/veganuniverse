package org.codingforanimals.veganuniverse.recipes.services.firebase.impl

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.recipes.services.firebase.IncrementRecipeLikesService
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection
import org.codingforanimals.veganuniverse.services.firebase.FirestoreFields
import org.codingforanimals.veganuniverse.storage.firestore.DocumentSnapshotCache

internal class IncrementRecipeLikesFirebaseService(
    private val firestore: FirebaseFirestore,
    private val recipesCache: DocumentSnapshotCache,
) : IncrementRecipeLikesService {
    override suspend fun increase(id: String) {
        firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .document(id)
            .update(FirestoreFields.Recipes.LIKES, FieldValue.increment(1))
            .await()
        updateRecipe(id)
    }

    override suspend fun decrease(id: String) {
        firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .document(id)
            .update(FirestoreFields.Recipes.LIKES, FieldValue.increment(-1))
            .await()
        updateRecipe(id)
    }

    private suspend fun updateRecipe(id: String) {
        val updatedRecipe = firestore
            .collection(FirestoreCollection.Content.Recipes.ITEMS)
            .document(id).get().await()
        recipesCache.putRecipe(updatedRecipe)
    }
}