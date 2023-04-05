package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02

@Composable
fun InteractiveRatingBar(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    var rating by remember { mutableStateOf(0) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing_02)
    ) {
        (1..5).forEach { step ->
            val icon = when {
                rating >= step -> VUIcons.StarFilled
                else -> VUIcons.Star
            }
            IconButton(
                onClick = { rating = step },
            ) {
                Icon(
                    painter = painterResource(icon.id),
                    contentDescription = "",
                    tint = color
                )
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
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
                modifier = Modifier.size(17.dp),
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
            3.8f,
            modifier = Modifier.height(20.dp)
        )
    }
}