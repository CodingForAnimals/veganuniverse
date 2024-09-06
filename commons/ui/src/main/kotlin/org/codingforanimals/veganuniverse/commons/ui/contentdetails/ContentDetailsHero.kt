package org.codingforanimals.veganuniverse.commons.ui.contentdetails

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.designsystem.LightBlue
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Success
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

sealed class ContentDetailsHeroImageType {
    data class Image(val url: String?) : ContentDetailsHeroImageType()
    data class Text(val text: String, val containerColor: Color) : ContentDetailsHeroImageType()
}

@Composable
fun ContentDetailsHero(
    imageType: ContentDetailsHeroImageType,
    icon: Icon,
    onImageClick: () -> Unit,
    colors: ContentDetailsHeroColors = ContentDetailsHeroDefaults.primaryColors(),
) {
    Box {
        val heroImageModifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .padding(bottom = 20.dp)
            .clickable(onClick = onImageClick)

        when (imageType) {
            is ContentDetailsHeroImageType.Image -> {
                AsyncImage(
                    modifier = heroImageModifier,
                    contentScale = ContentScale.Crop,
                    model = imageType.url,
                    contentDescription = null,
                )
            }
            is ContentDetailsHeroImageType.Text -> {
                Box(heroImageModifier) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = Spacing_05)
                            .aspectRatio(1f)
                            .align(Alignment.Center)
                            .clip(CardDefaults.shape)
                            .background(imageType.containerColor)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(Spacing_04)
                                .align(Alignment.Center),
                            text = imageType.text,
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                        )
                    }
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
                                color = colors.iconContainerBorder,
                                style = Stroke(3.dp.toPx())
                            )
                        },
                    )
                    VUIcon(
                        modifier = Modifier.align(Alignment.Center),
                        icon = icon,
                        contentDescription = "",
                        tint = colors.iconTint,
                    )
                }
            }
        }
    }
}

object ContentDetailsHeroDefaults {
    @Composable
    fun primaryColors() = ContentDetailsHeroColors(
        divider = MaterialTheme.colorScheme.primary,
        iconContainer = MaterialTheme.colorScheme.primary,
        iconTint = MaterialTheme.colorScheme.surfaceVariant,
        iconContainerBorder = MaterialTheme.colorScheme.surfaceVariant,
    )

    @Composable
    fun secondaryColors() = ContentDetailsHeroColors(
        divider = MaterialTheme.colorScheme.secondaryContainer,
        iconContainer = MaterialTheme.colorScheme.secondaryContainer,
        iconTint = MaterialTheme.colorScheme.surfaceVariant,
        iconContainerBorder = MaterialTheme.colorScheme.secondaryContainer,
    )

    @Composable
    fun errorColors() = ContentDetailsHeroColors(
        divider = MaterialTheme.colorScheme.error,
        iconContainer = MaterialTheme.colorScheme.surfaceVariant,
        iconTint = MaterialTheme.colorScheme.error,
        iconContainerBorder = MaterialTheme.colorScheme.error,
    )

    @Composable
    fun successColors() = ContentDetailsHeroColors(
        divider = Success,
        iconContainer = Color.White,
        iconTint = Success,
        iconContainerBorder = Success,
    )
}

data class ContentDetailsHeroColors(
    val divider: Color,
    val iconContainer: Color,
    val iconContainerBorder: Color,
    val iconTint: Color,
)

@Preview
@Composable
private fun PreviewContentDetailsHero() {
    VeganUniverseTheme {
        Surface {
            Column(
                modifier = Modifier.padding(Spacing_06),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                ContentDetailsHero(
                    imageType = ContentDetailsHeroImageType.Image(null),
                    icon = VUIcons.RecipesFilled,
                    onImageClick = {},
                )
                ContentDetailsHero(
                    imageType = ContentDetailsHeroImageType.Text("INS 311", LightBlue),
                    icon = VUIcons.ProductConfirmedVegan,
                    onImageClick = {},
                )
            }
        }
    }
}
