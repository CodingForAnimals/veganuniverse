package org.codingforanimals.veganuniverse.commons.place.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.shimmer
import org.codingforanimals.veganuniverse.commons.designsystem.PrimaryLight
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.place.presentation.model.PlaceCardUI
import org.codingforanimals.veganuniverse.commons.place.presentation.model.toUI
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceType
import org.codingforanimals.veganuniverse.commons.ui.components.RatingBar
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
fun PlaceCard(
    modifier: Modifier = Modifier,
    placeCard: PlaceCardUI,
    border: BorderStroke? = null,
    onCardClick: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = { onCardClick(placeCard.geoHash) }),
        border = border,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing_04, vertical = Spacing_05),
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing_02)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
                    ) {
                        val typeUI = (placeCard.type ?: PlaceType.STORE).toUI()
                        VUIcon(icon = typeUI.icon, contentDescription = null)
                        Text(
                            text = placeCard.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    RatingBar(
                        rating = placeCard.rating?.toInt() ?: 0,
                        color = PrimaryLight,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing_02),
                ) {
                    VUIcon(icon = VUIcons.Location, contentDescription = "")
                    Text(
                        text = placeCard.fullStreetAddress,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Box(modifier = Modifier.weight(0.7f)) {
                var loading by rememberSaveable { mutableStateOf(true) }
                AsyncImage(
                    modifier = Modifier
                        .clip(ShapeDefaults.Medium)
                        .placeholder(
                            visible = loading,
                            color = MaterialTheme.colorScheme.surface,
                            highlight = PlaceholderHighlight.shimmer(MaterialTheme.colorScheme.surfaceVariant)
                        )
                        .fillMaxSize(),
                    model = placeCard.imageUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    onState = { state ->
                        loading = when (state) {
                            is AsyncImagePainter.State.Success -> false
                            else -> true
                        }
                    }
                )
            }
        }
    }
}