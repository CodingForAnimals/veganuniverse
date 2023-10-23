package org.codingforanimals.veganuniverse.recipes.services

interface RecipeLikesService {
    suspend fun isRecipeLikedByUser(recipeId: String, userId: String): Boolean
    suspend fun dislikeReturningCurrent(recipeId: String, userId: String): Boolean
    suspend fun likeReturningCurrent(recipeId: String, userId: String): Boolean
    suspend fun isRecipeBookmarkedByUser(recipeId: String, userId: String): Boolean
    suspend fun bookmarkReturningCurrent(recipeId: String, userId: String): Boolean
    suspend fun unbookmarkReturningCurrent(recipeId: String, userId: String): Boolean
}

