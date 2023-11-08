package org.codingforanimals.veganuniverse.places.services.firebase.impl

import android.util.Log
import android.util.LruCache
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.services.firebase.FetchPlaceService
import org.codingforanimals.veganuniverse.places.services.firebase.entity.Place
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection
import org.codingforanimals.veganuniverse.places.entity.Place as PlaceDomainEntity

private const val TAG = "FetchPlaceFirebaseServi"

internal class FetchPlaceFirebaseService(
    private val firestore: FirebaseFirestore,
    private val placesCache: LruCache<String, DocumentSnapshot>,
    private val placeToDomainEntityMapper: OneWayEntityMapper<Place, PlaceDomainEntity>,
) : FetchPlaceService {
    override suspend fun byId(id: String): PlaceDomainEntity? {
        return (
            placesCache.get(id) ?: getFromFirestore(id)?.also { placesCache.put(id, it) }
            )
            ?.toPlaceDomainEntity()
    }

    override suspend fun byIds(ids: List<String>): List<PlaceDomainEntity> = coroutineScope {
        val deferreds = ids.map { id ->
            async { byId(id) }
        }
        val result = awaitAll(*deferreds.toTypedArray())
        result.mapNotNull { it }
    }

    private suspend fun getFromFirestore(id: String): DocumentSnapshot? {
        return firestore
            .collection(FirestoreCollection.Content.Places.ITEMS)
            .document(id)
            .get().await()
    }

    private fun DocumentSnapshot.toPlaceDomainEntity(): PlaceDomainEntity? {
        return try {
            toObject(Place::class.java)?.let { placeToDomainEntityMapper.map(it) }
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }
}
