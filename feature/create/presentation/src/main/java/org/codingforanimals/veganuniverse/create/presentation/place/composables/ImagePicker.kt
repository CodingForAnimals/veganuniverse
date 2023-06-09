package org.codingforanimals.veganuniverse.create.presentation.place.composables

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.Action

@Composable
internal fun ImagePicker(
    imageUri: Uri?,
    bitmap: Bitmap?,
    onAction: (Action) -> Unit,
) {
    val model = remember(imageUri, bitmap) { imageUri ?: bitmap }
    val imageModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(2f)
        .padding(horizontal = Spacing_06)
        .clip(ShapeDefaults.Medium)
        .clickable { onAction(Action.OnImagePickerClick) }
    if (model != null) {
        AsyncImage(
            modifier = imageModifier,
            contentScale = ContentScale.Crop,
            model = model,
            contentDescription = "",
        )
    } else {
        Column(
            modifier = imageModifier.background(MaterialTheme.colorScheme.surfaceVariant),
            verticalArrangement = Arrangement.spacedBy(
                Spacing_03, Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            VUIcon(
                modifier = Modifier.size(24.dp),
                icon = VUIcons.Pictures,
                contentDescription = "",
//                tint = colors.galeryIconTint,
            )
            Text(
                text = "Subir foto",
//                color = colors.galeryTextColor,
            )
        }
    }
}