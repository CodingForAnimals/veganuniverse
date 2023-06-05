package org.codingforanimals.veganuniverse.create.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.create.domain.place.PlaceFormDomainEntity

private const val PLACES_COLLECTION = "/content/places/cards"

class ContentCreatorImpl : ContentCreator {

    private val firebase = FirebaseFirestore.getInstance()


    override suspend fun createPlace(form: PlaceFormDomainEntity): Result<Unit> =
        runCatching {
            val existingPlaceQueryResult =
                firebase.collection(PLACES_COLLECTION).whereEqualTo("id", form.id).get().await()
            if (!existingPlaceQueryResult.isEmpty) {
                throw ContentCreatorException.PlaceAlreadyExists
            }
            firebase.collection(PLACES_COLLECTION).add(form.toFirebaseEntity()).await()
            return@runCatching
        }
}

sealed class ContentCreatorException : Exception() {
    object PlaceAlreadyExists : ContentCreatorException()
}

private data class PlaceFormFirebaseEntity(
    val id: String,
    val name: String,
    val openingHours: String,
    val type: String,
    val address: String,
    val city: String,
    val tags: List<String>,
    val geoHash: String,
    @ServerTimestamp val timestamp: Timestamp? = null,
)

private fun PlaceFormDomainEntity.toFirebaseEntity() = PlaceFormFirebaseEntity(
    id, name, openingHours, type, address, city, tags, geoHash
)