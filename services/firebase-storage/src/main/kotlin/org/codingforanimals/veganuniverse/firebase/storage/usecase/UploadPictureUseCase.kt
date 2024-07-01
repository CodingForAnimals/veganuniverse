package org.codingforanimals.veganuniverse.firebase.storage.usecase

import android.os.Parcelable
import org.codingforanimals.veganuniverse.firebase.storage.model.ResizeResolution

interface UploadPictureUseCase {
    suspend operator fun invoke(
        fileFolderPath: String,
        model: Parcelable,
    ): String
}

