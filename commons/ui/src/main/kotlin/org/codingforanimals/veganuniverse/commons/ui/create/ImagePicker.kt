package org.codingforanimals.veganuniverse.commons.ui.create

import android.os.Parcelable
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.ui.R
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    imageModel: Parcelable?,
    isError: Boolean,
    onClick: () -> Unit,
) {
    val color = when {
        isError -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outline
    }
    Crossfade(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        targetState = imageModel != null,
        label = "image_picker",
    ) { imageSelected ->
        if (imageSelected) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = imageModel,
                contentDescription = "",
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                verticalArrangement = Arrangement.spacedBy(Spacing_03, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                VUIcon(
                    modifier = Modifier.size(24.dp),
                    icon = VUIcons.Pictures,
                    contentDescription = "",
                    tint = color,
                )
                Text(
                    text = stringResource(R.string.image_picker_label),
                    color = color,
                )
            }
        }
    }
}