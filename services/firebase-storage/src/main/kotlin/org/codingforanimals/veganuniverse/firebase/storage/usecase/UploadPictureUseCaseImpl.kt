package org.codingforanimals.veganuniverse.firebase.storage.usecase

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.firebase.storage.model.ResizeResolution
import java.io.ByteArrayOutputStream

internal class UploadPictureUseCaseImpl(
    private val storage: FirebaseStorage,
) : UploadPictureUseCase {
    private val storageImageMetadata =
        StorageMetadata.Builder().setContentType("image/jpeg").build()

    override suspend operator fun invoke(
        fileFolderPath: String,
        model: Parcelable,
    ): String {
        val imageId = getRandomString()
        val imageStorageRef = storage.getReference(fileFolderPath).child(imageId)
        when (model) {
            is Bitmap -> {
                val bytes = ByteArrayOutputStream()
                model.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                imageStorageRef.putBytes(
                    bytes.toByteArray(),
                    storageImageMetadata,
                )

            }

            is Uri -> {
                imageStorageRef.putFile(
                    model,
                    storageImageMetadata
                )
            }

            else -> {
                throw RuntimeException("Unsupported image format")
            }
        }.await()
        return imageId
    }

    private fun getRandomString(): String {
        val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(20) { allowedChars.random() }.joinToString("")
    }
}