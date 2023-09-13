package org.codingforanimals.veganuniverse.profile.presentation.usecase

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.profile.domain.ProfileRepository

private const val TAG = "UploadNewProfilePicture"

class UploadNewProfilePictureUseCase(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(uri: Uri?): Flow<UploadNewProfilePictureStatus> = flow {
        if (uri == null) return@flow emit(UploadNewProfilePictureStatus.Error)
        emit(UploadNewProfilePictureStatus.Loading)
        try {
            profileRepository.uploadNewProfilePicture(uri)
            emit(UploadNewProfilePictureStatus.Success)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            emit(UploadNewProfilePictureStatus.Error)
        }
    }
}

sealed class UploadNewProfilePictureStatus {
    data object Loading : UploadNewProfilePictureStatus()
    data object Error : UploadNewProfilePictureStatus()
    data object Success : UploadNewProfilePictureStatus()
}