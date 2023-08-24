package org.codingforanimals.veganuniverse.places.presentation.home.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.PrimaryLight
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05

@Composable
internal fun PlaceCard(
    modifier: Modifier = Modifier,
    placeViewEntity: org.codingforanimals.veganuniverse.places.presentation.entity.PlaceCard,
    border: BorderStroke? = null,
    onCardClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(all = Spacing_04)
            .clickable(onClick = onCardClick),
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
                        VUIcon(icon = placeViewEntity.type.icon, contentDescription = "")
                        Text(
                            text = placeViewEntity.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    RatingBar(
                        rating = placeViewEntity.rating,
                        color = PrimaryLight,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing_02),
                ) {
                    VUIcon(icon = VUIcons.Location, contentDescription = "")
                    Text(
                        text = placeViewEntity.fullStreetAddress,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
//                Text(
//                    text = city,
//                    style = MaterialTheme.typography.bodyMedium,
//                )
            }
            Box(modifier = Modifier.weight(0.7f)) {
                Image(
                    modifier = Modifier
                        .clip(ShapeDefaults.Medium)
                        .fillMaxSize(),
                    painter = painterResource(R.drawable.vegan_restaurant),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}
