package org.codingforanimals.veganuniverse.profile.domain

import android.net.Uri

interface ProfileRepository {
    suspend fun uploadNewProfilePicture(uri: Uri)
}