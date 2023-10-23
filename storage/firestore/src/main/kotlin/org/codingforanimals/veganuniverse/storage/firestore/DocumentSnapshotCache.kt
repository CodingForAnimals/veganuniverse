package org.codingforanimals.veganuniverse.storage.firestore

import com.google.firebase.firestore.DocumentSnapshot

interface DocumentSnapshotCache {
    fun getRecipe(id: String): DocumentSnapshot?
    fun putRecipe(recipe: DocumentSnapshot): Boolean
}