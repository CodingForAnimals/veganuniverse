@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.core.ui.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTag
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06

data class ItemDetailHeroColors(
    val imageBackground: Color,
    val divider: Color,
    val iconContainer: Color,
    val iconTint: Color,
    val iconBorder: Color,
) {
    companion object ItemDetailHeroDefaults {
        @Composable
        fun primaryColors() = ItemDetailHeroColors(
            imageBackground = MaterialTheme.colorScheme.surfaceVariant,
            divider = MaterialTheme.colorScheme.primary,
            iconContainer = MaterialTheme.colorScheme.primary,
            iconTint = MaterialTheme.colorScheme.surfaceVariant,
            iconBorder = MaterialTheme.colorScheme.surfaceVariant,
        )

        @Composable
        fun secondaryColors() = ItemDetailHeroColors(
            imageBackground = MaterialTheme.colorScheme.surfaceVariant,
            divider = MaterialTheme.colorScheme.secondaryContainer,
            iconContainer = MaterialTheme.colorScheme.secondaryContainer,
            iconTint = MaterialTheme.colorScheme.surfaceVariant,
            iconBorder = MaterialTheme.colorScheme.secondaryContainer,
        )
    }
}

@Composable
fun ItemDetailHero(
    imageRes: Int? = null,
    icon: Icon,
    onImageClick: () -> Unit,
    colors: ItemDetailHeroColors = ItemDetailHeroColors.primaryColors(),
) {
    Box {
        val heroImageModifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .padding(bottom = 20.dp)
            .clickable(onClick = onImageClick)
        if (imageRes != null) {
            Image(
                modifier = heroImageModifier,
                painter = painterResource(imageRes), contentDescription = "",
                contentScale = ContentScale.Crop,
            )
        } else {
            Column(
                modifier = heroImageModifier.background(colors.imageBackground),
                verticalArrangement = Arrangement.spacedBy(Spacing_03, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                VUIcon(
                    modifier = Modifier.size(24.dp),
                    icon = VUIcons.Pictures,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
                Text(
                    text = "Subir foto",
                    color = MaterialTheme.colorScheme.primaryContainer,
                )
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
                                color = colors.iconBorder,
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

@Composable
fun FeatureItemTitle(
    title: String,
    subtitle: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier.padding(start = Spacing_06, end = Spacing_06, bottom = Spacing_04),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            subtitle()
        }
        Row {
            VUIcon(icon = VUIcons.Share, contentDescription = "", onIconClick = {})
            VUIcon(icon = VUIcons.Bookmark, contentDescription = "", onIconClick = {})
        }
    }
}

@Composable
fun UserInfo() {
    Row(
        modifier = Modifier.padding(horizontal = Spacing_06),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.vegan_restaurant),
            contentDescription = "Imágen del usuario creador del post",
        )
        Column {
            Text(text = "@PizzaMuzza", fontWeight = FontWeight.SemiBold)
            Text(text = "Pablo Rago")
        }
    }
}

@Composable
fun FeatureItemTags(
    tags: List<String>
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(unbounded = true)
            .padding(horizontal = Spacing_06),
        horizontalArrangement = Arrangement.spacedBy(Spacing_06),
    ) {
        tags.forEach { VUTag(label = it) }
    }
}

@Composable
fun FeatureItemComments() {
    val comments = listOf(
        Pair("Nacho", "Hola! Cuánto dura en la heladera?"),
        Pair("Pizza Muzza", "Una semana aproximadamente"),
    )
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Text(
            text = "¿Qué dice la comunidad?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = Spacing_06),
        )
        comments.forEach {
            Post(
                modifier = Modifier.padding(horizontal = Spacing_06),
                title = it.first,
                subtitle = it.second
            )
        }
    }
}