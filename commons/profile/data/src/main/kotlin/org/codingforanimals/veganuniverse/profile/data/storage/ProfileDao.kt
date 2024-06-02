package org.codingforanimals.veganuniverse.profile.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditActionType
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditContentType

@Dao
interface ProfileDao {
    @Query("SELECT * FROM ProfileContent")
    suspend fun getAllProfileContent(): List<ProfileContent>

    @Query("SELECT * FROM ProfileContent WHERE contentId = :contentId AND contentType = :contentType AND actionType = :actionType")
    suspend fun getProfileContent(
        contentId: String,
        contentType: ProfileEditContentType,
        actionType: ProfileEditActionType,
    ): List<ProfileContent>

    @Insert
    suspend fun insertProfileContent(vararg profileContent: ProfileContent)

    @Query("DELETE FROM ProfileContent WHERE contentId = :contentId AND contentType = :contentType AND actionType = :actionType")
    suspend fun deleteProfileContent(
        contentId: String,
        contentType: ProfileEditContentType,
        actionType: ProfileEditActionType,
    ): Int

    @Query("DELETE FROM ProfileContent")
    suspend fun clearAllProfileContent()
}
