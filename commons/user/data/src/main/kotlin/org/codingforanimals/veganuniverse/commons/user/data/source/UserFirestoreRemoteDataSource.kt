package org.codingforanimals.veganuniverse.commons.user.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.commons.user.data.model.User

private const val TAG = "UserInfoFirestoreDataSo"

internal class UserFirestoreRemoteDataSource(
    firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : UserRemoteDataSource {
    private val usersCollection = firestore.collection(USERS_COLLECTION)

    override suspend fun createUser(email: String, name: String): User {
        val userId = auth.currentUser!!.uid
        val user = User(
            userId = userId,
            name = name,
            email = email,
            isEmailVerified = false,
        )
        usersCollection.document().set(user).await()
        return user
    }

    override suspend fun getUser(userId: String): User? {
        return runCatching {
            getUserFromFirestore(userId)
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
        }.getOrNull()
    }

    override suspend fun getCurrentUser(): User? {
        return runCatching {
            val currentUser = auth.currentUser ?: return null
            val userDTO = getUserFromFirestore(currentUser.uid)
            userDTO?.copy(isEmailVerified = currentUser.isEmailVerified)
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
        }.getOrNull()
    }

    private suspend fun getUserFromFirestore(userId: String): User? {
        return usersCollection.whereEqualTo(USER_ID, userId)
            .get().await()
            .documents.firstOrNull()?.toObject(User::class.java)
    }

    override suspend fun reloadUser() {
        auth.currentUser?.reload()?.await()
    }

    override suspend fun sendVerificationEmail() {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("Guest user attempting to send verification email")
        currentUser.sendEmailVerification().await()
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val USER_ID = "userId"
    }
}
