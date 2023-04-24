package org.codingforanimals.veganuniverse.create.domain

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.codingforanimals.veganuniverse.create.domain.place.PlaceFormDomainEntity

class ContentCreatorImpl : ContentCreator {

    private val firebase = FirebaseFirestore.getInstance()

    override suspend fun createPlace(form: PlaceFormDomainEntity): Result<Unit> =
        runCatching {
            withTimeout(5000) {
                firebase.collection("/content/places/cards").add(form).await()
            }
        }
}