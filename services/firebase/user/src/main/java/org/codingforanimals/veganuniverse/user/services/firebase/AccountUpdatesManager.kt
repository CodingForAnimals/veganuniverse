package org.codingforanimals.veganuniverse.user.services.firebase

import android.net.Uri

interface AccountUpdatesManager {
    suspend fun updateProfilePicture(uri: Uri)
}