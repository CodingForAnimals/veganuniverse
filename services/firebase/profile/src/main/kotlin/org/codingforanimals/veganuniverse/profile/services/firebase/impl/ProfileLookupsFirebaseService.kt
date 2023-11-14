package org.codingforanimals.veganuniverse.profile.services.firebase.impl

import android.util.LruCache
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService
import org.codingforanimals.veganuniverse.services.firebase.DatabasePath

internal class ProfileLookupsFirebaseService(
    private val database: FirebaseDatabase,
    private val savedContentCache: LruCache<String, List<String>>,
) : ProfileLookupsService {

    override suspend fun isContentSavedByUser(
        contentId: String,
        saveableType: SaveableType,
        contentType: SaveableContentType,
        userId: String,
    ): Boolean {
        val savedContent = getContentSavedByUser(saveableType, contentType, userId)
        return savedContent.contains(contentId)
    }

    override suspend fun saveContent(
        contentId: String,
        saveableType: SaveableType,
        contentType: SaveableContentType,
        userId: String,
    ) {
        val path = getSaveableContentPath(saveableType, contentType)
        database
            .getReference(path)
            .child(userId)
            .child(contentId)
            .setValue(true)
            .await()

        val cacheKey = getSaveableContentCacheKey(saveableType, contentType)
        val currentCachedSavedContent = savedContentCache.get(cacheKey)?.toMutableList()
        if (currentCachedSavedContent.isNullOrEmpty()) {
            savedContentCache.put(cacheKey, listOf(contentId))
        } else {
            currentCachedSavedContent.add(contentId)
            savedContentCache.put(cacheKey, currentCachedSavedContent)
        }
    }

    override suspend fun removeContent(
        contentId: String,
        saveableType: SaveableType,
        contentType: SaveableContentType,
        userId: String,
    ) {
        val path = getSaveableContentPath(saveableType, contentType)
        database
            .getReference(path)
            .child(userId)
            .child(contentId)
            .removeValue()
            .await()

        val cacheKey = getSaveableContentCacheKey(saveableType, contentType)
        val currentCachedSavedContent = savedContentCache.get(cacheKey)?.toMutableList()
        if (!currentCachedSavedContent.isNullOrEmpty()) {
            currentCachedSavedContent.remove(contentId)
            savedContentCache.put(cacheKey, currentCachedSavedContent)
        }
    }

    override suspend fun getContentSavedByUser(
        saveableType: SaveableType,
        contentType: SaveableContentType,
        userId: String,
    ): List<String> {
        val cacheKey = getSaveableContentCacheKey(saveableType, contentType)
        val cachedSavedContentIds = savedContentCache.get(cacheKey)?.distinct()
        return if (cachedSavedContentIds.isNullOrEmpty()) {
            val path = getSaveableContentPath(saveableType, contentType)
            val savedContentIdsMap = database
                .getReference(path)
                .child(userId)
                .get().await()
                .children
            val remoteSavedContentIds = savedContentIdsMap.mapNotNull { it.key }
            savedContentCache.put(cacheKey, remoteSavedContentIds.distinct())
            remoteSavedContentIds
        } else {
            cachedSavedContentIds
        }
    }

    private fun getSaveableContentPath(
        saveableType: SaveableType,
        contentType: SaveableContentType,
    ): String {
        return when (saveableType) {
            SaveableType.LIKE -> {
                when (contentType) {
                    SaveableContentType.RECIPE -> DatabasePath.Profile.Likes.RECIPES
                    SaveableContentType.PLACE -> DatabasePath.Profile.Likes.PLACES
                }
            }

            SaveableType.BOOKMARK ->
                when (contentType) {
                    SaveableContentType.RECIPE -> DatabasePath.Profile.Bookmarks.RECIPES
                    SaveableContentType.PLACE -> DatabasePath.Profile.Bookmarks.PLACES
                }

            SaveableType.CONTRIBUTION ->
                when (contentType) {
                    SaveableContentType.RECIPE -> DatabasePath.Profile.Contributions.RECIPES
                    SaveableContentType.PLACE -> DatabasePath.Profile.Contributions.PLACES
                }
        }
    }

    private fun getSaveableContentCacheKey(
        saveableType: SaveableType,
        contentType: SaveableContentType,
    ): String {
        return "${saveableType.name}_${contentType.name}"
    }
}