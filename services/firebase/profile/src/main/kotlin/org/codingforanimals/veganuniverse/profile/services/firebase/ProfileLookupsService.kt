package org.codingforanimals.veganuniverse.profile.services.firebase

import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType

interface ProfileLookupsService {
    suspend fun isContentSavedByUser(
        contentId: String,
        saveableType: SaveableType,
        contentType: SaveableContentType,
        userId: String,
    ): Boolean

    suspend fun saveContent(
        contentId: String,
        saveableType: SaveableType,
        contentType: SaveableContentType,
        userId: String,
    )

    suspend fun removeContent(
        contentId: String,
        saveableType: SaveableType,
        contentType: SaveableContentType,
        userId: String,
    )

    suspend fun getContentSavedByUser(
        saveableType: SaveableType,
        contentType: SaveableContentType,
        userId: String,
    ): List<String>
}