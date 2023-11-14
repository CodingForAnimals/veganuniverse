package org.codingforanimals.veganuniverse.profile.home.domain

import android.net.Uri

interface ProfileRepository {
    suspend fun uploadNewProfilePicture(uri: Uri)
}