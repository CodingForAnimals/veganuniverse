package org.codingforanimals.veganuniverse.user.services.firebase.impl

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.services.firebase.StoragePath
import org.codingforanimals.veganuniverse.services.firebase.storageImageMetadata
import org.codingforanimals.veganuniverse.user.services.firebase.AccountUpdatesManager

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

    /**
     * update user name recursively on comments
     */
//    init {
//
//        /**
//         * update user name
//         */
//        CoroutineScope(Dispatchers.IO).launch {
//            val userCommentsDeferred = database
//                .getReference("content/recipes/userComments/elpepeargento")
//                .get().asDeferred()
//
//            awaitAll(userCommentsDeferred)
//
//            val userComments = mutableMapOf<String, String>()
//            userCommentsDeferred.await()
//                .children
//                .forEach { snap ->
//                    val (commentId, recipeID) = Pair(snap.key, snap.value)
//                    if (commentId != null && recipeID != null) {
//                        userComments[commentId] = recipeID.toString()
//                    }
//                }
//
//            val usernameUpdates = mutableMapOf<String, String>()
//            userComments.forEach { (commentId, recipeId) ->
//                usernameUpdates["content/recipes/comments/$recipeId/$commentId/u_n"] =
//                    "nuevo nombre pa!!!"
//            }
//            database.reference.updateChildren(
//                usernameUpdates.toMap()
//            ).await()
//        }
//    }
}