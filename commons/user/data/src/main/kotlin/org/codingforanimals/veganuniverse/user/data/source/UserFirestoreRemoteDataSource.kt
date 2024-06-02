package org.codingforanimals.veganuniverse.user.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.user.data.dto.UserDTO

private const val TAG = "UserInfoFirestoreDataSo"

internal class UserFirestoreRemoteDataSource(
    firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : UserRemoteDataSource {
    private val usersCollection = firestore.collection(USERS_COLLECTION)
    override suspend fun getUser(userId: String): UserDTO? {
        return runCatching {
            getUserFromFirestore(userId)
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
        }.getOrNull()
    }

    override suspend fun getCurrentUser(userId: String): UserDTO? {
        return runCatching {
            val currentUser = auth.currentUser ?: return null
            val userDTO = getUserFromFirestore(userId)
            userDTO?.copy(isEmailVerified = currentUser.isEmailVerified)
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
        }.getOrNull()
    }

    private suspend fun getUserFromFirestore(userId: String): UserDTO? {
        return usersCollection.whereEqualTo(USER_ID, userId)
            .get().await()
            .documents.firstOrNull()?.toObject(UserDTO::class.java)
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val USER_ID = "userId"
    }
}
