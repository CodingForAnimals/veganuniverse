package org.codingforanimals.veganuniverse.commons.ui.contentdetails

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
fun ContentDetailsHero(
    imageUri: Uri? = null,
    url: String? = null,
    icon: Icon,
    onImageClick: () -> Unit,
    colors: ContentDetailsHeroColors = ContentDetailsHeroColors.primaryColors(),
) {

    val uri = rememberAsyncImagePainter(imageUri)
    Box {
        val heroImageModifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .padding(bottom = 20.dp)
            .clickable(onClick = onImageClick)
        when {
            imageUri != null -> {
                Image(
                    modifier = heroImageModifier,
                    painter = uri, contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }

            url != null -> {
                AsyncImage(
                    modifier = heroImageModifier,
                    contentScale = ContentScale.Crop,
                    model = url,
                    contentDescription = null,
                )
            }

            else -> {
                Column(
                    modifier = heroImageModifier.background(colors.imageBackground),
                    verticalArrangement = Arrangement.spacedBy(
                        Spacing_03,
                        Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    VUIcon(
                        modifier = Modifier.size(24.dp),
                        icon = VUIcons.Pictures,
                        contentDescription = "",
                        tint = colors.galleryIconTint,
                    )
                    Text(
                        text = "Subir foto",
                        color = colors.galleryTextColor,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.BottomCenter),
        ) {
            Spacer(
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(4.dp)
                    .fillMaxWidth()
                    .background(colors.divider)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = Spacing_06),
            ) {
                Box {
                    Canvas(
                        modifier = Modifier.size(30.dp),
                        onDraw = {
                            drawCircle(
                                radius = 20.dp.toPx(),
                                color = colors.iconContainer,
                            )
                            drawCircle(
                                radius = 20.dp.toPx(),
                                color = colors.typeIconBorder,
                                style = Stroke(3.dp.toPx())
                            )
                        },
                    )
                    VUIcon(
                        modifier = Modifier.align(Alignment.Center),
                        icon = icon,
                        contentDescription = "",
                        tint = colors.typeIconTint,
                    )
                }
            }
        }
    }
}

data class ContentDetailsHeroColors(
    val contentColor: Color,
    val imageBackground: Color,
    val divider: Color,
    val iconContainer: Color,
    val typeIconTint: Color,
    val typeIconBorder: Color,
    val galleryIconTint: Color,
    val galleryTextColor: Color,
) {
    companion object ItemDetailHeroDefaults {
        @Composable
        fun primaryColors() = ContentDetailsHeroColors(
            contentColor = MaterialTheme.colorScheme.primary,
            imageBackground = MaterialTheme.colorScheme.surfaceVariant,
            divider = MaterialTheme.colorScheme.primary,
            iconContainer = MaterialTheme.colorScheme.primary,
            typeIconTint = MaterialTheme.colorScheme.surfaceVariant,
            typeIconBorder = MaterialTheme.colorScheme.surfaceVariant,
            galleryIconTint = MaterialTheme.colorScheme.primaryContainer,
            galleryTextColor = MaterialTheme.colorScheme.primaryContainer,
        )

        @Composable
        fun secondaryColors() = ContentDetailsHeroColors(
            contentColor = MaterialTheme.colorScheme.primary,
            imageBackground = MaterialTheme.colorScheme.surfaceVariant,
            divider = MaterialTheme.colorScheme.secondaryContainer,
            iconContainer = MaterialTheme.colorScheme.secondaryContainer,
            typeIconTint = MaterialTheme.colorScheme.surfaceVariant,
            typeIconBorder = MaterialTheme.colorScheme.secondaryContainer,
            galleryIconTint = MaterialTheme.colorScheme.primaryContainer,
            galleryTextColor = MaterialTheme.colorScheme.primaryContainer,
        )

        @Composable
        fun errorColors() = ContentDetailsHeroColors(
            contentColor = MaterialTheme.colorScheme.error,
            imageBackground = MaterialTheme.colorScheme.surfaceVariant,
            divider = MaterialTheme.colorScheme.error,
            iconContainer = MaterialTheme.colorScheme.error,
            typeIconTint = MaterialTheme.colorScheme.surfaceVariant,
            typeIconBorder = MaterialTheme.colorScheme.secondaryContainer,
            galleryIconTint = MaterialTheme.colorScheme.error,
            galleryTextColor = MaterialTheme.colorScheme.error,
        )
    }
}
