package org.codingforanimals.veganuniverse.commons.ui.create

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon

@Composable
fun HeroAnchor(
    modifier: Modifier = Modifier,
    icon: Icon?,
    colors: HeroAnchorColors = HeroAnchorDefaults.secondaryColors(),
) {
    Box(
        modifier = modifier.height(40.dp),
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.Center)
                .height(4.dp)
                .fillMaxWidth()
                .background(colors.main)
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
                            color = colors.main,
                        )
                        drawCircle(
                            radius = 20.dp.toPx(),
                            color = colors.iconBorder,
                            style = Stroke(3.dp.toPx())
                        )
                    },
                )
                icon?.let {
                    VUIcon(
                        modifier = Modifier.align(Alignment.Center),
                        icon = it,
                        contentDescription = "",
                        tint = colors.icon,
                    )
                }
            }
        }
    }
}

data class HeroAnchorColors(
    val main: Color,
    val icon: Color,
    val iconBorder: Color,
)

object HeroAnchorDefaults {

    @Composable
    fun primaryColors(): HeroAnchorColors {
        return HeroAnchorColors(
            main = MaterialTheme.colorScheme.primary,
            icon = MaterialTheme.colorScheme.surfaceVariant,
            iconBorder = MaterialTheme.colorScheme.surfaceVariant,
        )
    }

    @Composable
    fun secondaryColors(): HeroAnchorColors {
        return HeroAnchorColors(
            main = MaterialTheme.colorScheme.outline,
            icon = MaterialTheme.colorScheme.surfaceVariant,
            iconBorder = MaterialTheme.colorScheme.outline,
        )
    }

    @Composable
    fun errorColors(): HeroAnchorColors {
        return HeroAnchorColors(
            main = MaterialTheme.colorScheme.error,
            icon = MaterialTheme.colorScheme.surfaceVariant,
            iconBorder = MaterialTheme.colorScheme.error,
        )
    }
}