package org.codingforanimals.veganuniverse.commons.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
fun InteractiveRatingBar(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing_02)
    ) {
        (1..5).forEach { starIndex ->
            val icon = when {
                value >= starIndex -> VUIcons.StarFilled
                else -> VUIcons.Star
            }
            IconButton(
                onClick = { onValueChange(starIndex) },
            ) {
                Icon(
                    modifier = Modifier.padding(Spacing_02),
                    painter = painterResource(icon.id),
                    contentDescription = "",
                    tint = color,
                )
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    starIconSize: Dp = 17.dp,
) {
    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(Spacing_02)
    ) {
        (1..5).forEach { step ->
            val icon = when {
                rating >= step -> VUIcons.StarFilled
                else -> VUIcons.Star
            }
            Icon(
                modifier = Modifier.size(starIconSize),
                painter = painterResource(icon.id),
                contentDescription = "",
                tint = color
            )
        }
    }
}

@Preview
@Composable
private fun RatingBarPreview() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        RatingBar(
            3,
            modifier = Modifier.height(20.dp)
        )
    }
}