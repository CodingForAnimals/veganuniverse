package org.codingforanimals.veganuniverse.storage.firestore

import android.util.Log
import android.util.LruCache
import com.google.firebase.firestore.DocumentSnapshot

private const val TAG = "DocumentSnapshotLruCach"

internal class DocumentSnapshotLruCache : LruCache<String, DocumentSnapshot>(4 * 1024 * 1024),
    DocumentSnapshotCache {
    override fun getRecipe(id: String): DocumentSnapshot? {
        return try {
            get(id)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }

    override fun putRecipe(recipe: DocumentSnapshot): Boolean {
        return try {
            put(recipe.id, recipe)
            true
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            false
        }
    }
}