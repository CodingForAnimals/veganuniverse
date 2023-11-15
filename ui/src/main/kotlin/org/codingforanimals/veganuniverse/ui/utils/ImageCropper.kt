package org.codingforanimals.veganuniverse.ui.utils

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions

private const val TAG = "ImageCropper"

@Composable
fun rememberImageCropperLauncherForActivityResult(
    onCropSuccess: (Uri?) -> Unit,
): ActivityResultLauncher<PickVisualMediaRequest> {
    val cropImage = rememberLauncherForActivityResult(
        contract = CropImageContract(),
        onResult = { result ->
            if (result.isSuccessful) {
                onCropSuccess(result.uriContent)
            } else {
                Log.e(TAG, result.error?.stackTraceToString() ?: "Unknown error cropping image")
            }
        },
    )

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                cropImage.launch(
                    CropImageContractOptions(
                        uri,
                        CropImageOptions(aspectRatioX = 1, aspectRatioY = 1, fixAspectRatio = true)
                    )
                )
            }
        },
    )
}