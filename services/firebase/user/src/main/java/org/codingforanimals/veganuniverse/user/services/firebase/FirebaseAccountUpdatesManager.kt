package org.codingforanimals.veganuniverse.user.services.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.services.firebase.StoragePath
import org.codingforanimals.veganuniverse.services.firebase.storageImageMetadata

internal class FirebaseAccountUpdatesManager(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
) : AccountUpdatesManager {

    override suspend fun updateProfilePicture(uri: Uri) {
        val userId = auth.currentUser!!.uid
        storage
            .getReference("${StoragePath.USERS_PICTURE_PATH}/$userId.jpeg")
            .putFile(uri, storageImageMetadata)
            .await()
    }
}