package org.codingforanimals.places.presentation.home.composables

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codingforanimals.places.presentation.home.PlaceCardViewEntity

@Composable
internal fun PlacesColumn(
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
    selectedPlaceIndex: Int?,
    places: List<PlaceCardViewEntity>,
    onCardClick: (Int) -> Unit,
) {
    val selectedPlace = selectedPlaceIndex?.let { places[it] }
    Crossfade(
        modifier = modifier,
        targetState = selectedPlace != null
    ) { isPlaceSelected ->
        if (isPlaceSelected) {
            selectedPlace?.apply {
                PlaceCard(
                    modifier = itemModifier,
                    name = name,
                    type = type,
                    rating = rating,
                    address = address,
                    city = city,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    onCardClick = { onCardClick(selectedPlaceIndex) },
                )
            }
        } else {
            Column {
                places.forEachIndexed { index, place ->
                    PlaceCard(
                        modifier = itemModifier,
                        name = place.name,
                        type = place.type,
                        rating = place.rating,
                        address = place.address,
                        city = place.city,
                        onCardClick = { onCardClick(index) },
                    )
                }
            }
        }
    }
}