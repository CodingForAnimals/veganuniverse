package org.codingforanimals.veganuniverse.commons.profile.data.storage

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditActionType
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditContentType

@Entity
data class ProfileContent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contentId: String,
    val contentType: ProfileEditContentType,
    val actionType: ProfileEditActionType,
)
