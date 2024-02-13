package org.codingforanimals.veganuniverse.commons.user.data.source

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.commons.user.data.dto.UserInfoDTO

private const val TAG = "UserInfoFirestoreDataSo"

internal class UserInfoFirestoreDataSource(
    firestore: FirebaseFirestore,
) : UserInfoDataSource {
    private val usersCollection = firestore.collection(USERS_COLLECTION)
    override suspend fun getUserInfo(userId: String): UserInfoDTO? {
        return runCatching {
            usersCollection.whereEqualTo(USER_ID, userId)
                .get().await()
                .documents.firstOrNull()?.toObject(UserInfoDTO::class.java)
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
        }.getOrNull()
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val USER_ID = "userId"
    }
}
