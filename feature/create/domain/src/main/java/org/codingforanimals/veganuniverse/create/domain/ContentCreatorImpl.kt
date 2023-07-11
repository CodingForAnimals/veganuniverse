package org.codingforanimals.veganuniverse.create.domain

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.create.domain.model.PlaceFormDomainEntity
import org.codingforanimals.veganuniverse.create.domain.model.toFirebaseEntity

private const val PLACES_COLLECTION = "/content/places/cards"

class ContentCreatorImpl : ContentCreator {

    private val firebase = FirebaseFirestore.getInstance()


    override suspend fun createPlace(form: PlaceFormDomainEntity): Result<Unit> =
        runCatching {
            val existingPlaceQueryResult =
                firebase.collection(PLACES_COLLECTION).whereEqualTo("geoHash", form.geoHash).get()
                    .await()
            if (!existingPlaceQueryResult.isEmpty) {
                throw ContentCreatorException.AlreadyExistsException
            }
            firebase.collection(PLACES_COLLECTION).add(form.toFirebaseEntity()).await()
            return@runCatching
        }
}

sealed class ContentCreatorException : Exception() {
    object AlreadyExistsException : ContentCreatorException()
}
