package org.codingforanimals.veganuniverse.create.presentation.place.composables

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.common.PictureField
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel

data class ImagePickerColors(
    val borderColor: Color,
    val contentColor: Color,
)

object ImagePickerDefaults {

    @Composable
    fun defaultColors() = ImagePickerColors(
        borderColor = MaterialTheme.colorScheme.outline,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    @Composable
    fun errorColors() = ImagePickerColors(
        borderColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.error,
    )

}

@Composable
internal fun ImagePicker(
    pictureField: PictureField,
    isValidating: Boolean,
    onAction: (CreatePlaceViewModel.Action) -> Unit,
) {
    val colors = if (isValidating && !pictureField.isValid) {
        ImagePickerDefaults.errorColors()
    } else {
        ImagePickerDefaults.defaultColors()
    }

    val imagePickerModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = Spacing_06)
        .aspectRatio(2f)
        .border(1.dp, colors.borderColor, ShapeDefaults.Medium)
        .clip(ShapeDefaults.Medium)
        .clickable { onAction(CreatePlaceViewModel.Action.OnImagePickerClick) }

    Crossfade(targetState = pictureField.isValid) {
        if (it) {
            AsyncImage(
                modifier = imagePickerModifier,
                contentScale = ContentScale.Crop,
                model = pictureField.model,
                contentDescription = "",
            )
        } else {
            Column(
                modifier = imagePickerModifier.background(MaterialTheme.colorScheme.surfaceVariant),
                verticalArrangement = Arrangement.spacedBy(
                    Spacing_03, Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                VUIcon(
                    modifier = Modifier.size(24.dp),
                    icon = VUIcons.Pictures,
                    contentDescription = "",
                    tint = colors.contentColor,
                )
                Text(
                    text = stringResource(R.string.image_picker_label),
                    color = colors.contentColor,
                )
            }
        }
    }
}