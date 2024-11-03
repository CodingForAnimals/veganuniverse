package org.codingforanimals.veganuniverse.firebase.storage.usecase

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

interface DeletePicture {
    suspend operator fun invoke(path: String)
}

internal class DeletePictureImpl(
    private val storage: FirebaseStorage,
) : DeletePicture {
    override suspend operator fun invoke(path: String) {
        storage.getReference(path).delete().await()
    }
}
