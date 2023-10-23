package org.codingforanimals.veganuniverse.recipes.services.impl

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.recipes.services.RecipeLikesService
import org.codingforanimals.veganuniverse.services.firebase.DatabasePath

internal class RecipeLikesFirebaseService(
    private val database: FirebaseDatabase,
) : RecipeLikesService {
    override suspend fun isRecipeLikedByUser(recipeId: String, userId: String): Boolean {
        val snap = database
            .getReference(DatabasePath.User.Likes.RECIPES)
            .child(userId)
            .child(recipeId)
            .get().await()
        return snap.exists()
    }

    override suspend fun dislikeReturningCurrent(recipeId: String, userId: String): Boolean {
        val ref = database
            .getReference(DatabasePath.User.Likes.RECIPES)
            .child(userId)
            .child(recipeId)
        ref.removeValue().await()
        return false
    }

    override suspend fun likeReturningCurrent(recipeId: String, userId: String): Boolean {
        val ref = database
            .getReference(DatabasePath.User.Likes.RECIPES)
            .child(userId)
            .child(recipeId)
        ref.setValue(true).await()
        return true
    }

    override suspend fun isRecipeBookmarkedByUser(recipeId: String, userId: String): Boolean {
        val snap = database
            .getReference(DatabasePath.User.Bookmarks.RECIPES)
            .child(userId)
            .child(recipeId)
            .get().await()
        return snap.exists()
    }

    override suspend fun bookmarkReturningCurrent(recipeId: String, userId: String): Boolean {
        val ref = database
            .getReference(DatabasePath.User.Bookmarks.RECIPES)
            .child(userId)
            .child(recipeId)
        ref.setValue(true).await()
        return true
    }

    override suspend fun unbookmarkReturningCurrent(recipeId: String, userId: String): Boolean {
        val ref = database
            .getReference(DatabasePath.User.Bookmarks.RECIPES)
            .child(userId)
            .child(recipeId)
        ref.removeValue().await()
        return false
    }

}