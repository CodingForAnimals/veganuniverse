@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.places.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import org.codingforanimals.veganuniverse.core.ui.components.InteractiveRatingBar
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.FeatureItemTags
import org.codingforanimals.veganuniverse.core.ui.shared.FeatureItemTitle
import org.codingforanimals.veganuniverse.core.ui.shared.GenericPost
import org.codingforanimals.veganuniverse.core.ui.shared.HeaderData
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHero
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06

@Composable
internal fun PlaceDetailsScreen(
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        VUTopAppBar(
            onBackClick = onBackClick,
            actions = {
                var showMenu by rememberSaveable { mutableStateOf(false) }
                VUIcon(
                    icon = VUIcons.MoreOptions,
                    contentDescription = "",
                    onIconClick = { showMenu = !showMenu },
                )
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text(text = "Reportar lugar") },
                        onClick = {},
                        leadingIcon = {
                            VUIcon(icon = VUIcons.Report, contentDescription = "")
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Sugerir edición") },
                        onClick = {},
                        leadingIcon = {
                            VUIcon(icon = VUIcons.Edit, contentDescription = "")
                        })
                }
            }
        )

        PlaceDetailsScreenData()
    }
}

@Composable
private fun PlaceDetailsScreenData() {
    val latlng = LatLng(-37.331928, -59.139309)
    val camera = CameraPositionState(
        position = CameraPosition.fromLatLngZoom(
            latlng,
            16f
        )
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        item {
            ItemDetailHero(
//                imageUri = org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant,
                icon = VUIcons.StoreFilled,
                onImageClick = {},
            )
        }
        item {
            FeatureItemTitle(
                title = "Todo Vegano",
                subtitle = { RatingBar(rating = 4) }
            )
        }
        item { CoreData() }
        item { Description() }
        item { FeatureItemTags(tags) }
        item { Map(camera) }
        item { AddReview() }
        item {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = Spacing_06),
                verticalArrangement = Arrangement.spacedBy(Spacing_04)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = Spacing_06),
                    text = "Reseñas de la comunidad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                reviews.forEach {
                    Review(it)
                }
            }
        }
    }
}

@Composable
private fun CoreData() {
    Column(
        modifier = Modifier.padding(horizontal = Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
            VUIcon(icon = VUIcons.Places, contentDescription = "")
            Text(text = "Bme. Mitre 696 - Monte Grande")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
            VUIcon(icon = VUIcons.Clock, contentDescription = "")
            Text(text = "Lun a Vie - 10 a 20 hs")
        }
    }
}

private val tags = listOf("Tienda", "Take away", "Sin tacc", "100% Vegan")

@Composable
private fun Description() {
    Text(
        modifier = Modifier.padding(horizontal = Spacing_06),
        text = description,
    )
}

@Composable
private fun Map(cameraPositionState: CameraPositionState) {
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_06)
            .aspectRatio(2f),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            compassEnabled = false,
            indoorLevelPickerEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false,
            rotationGesturesEnabled = false,
            scrollGesturesEnabled = false,
            scrollGesturesEnabledDuringRotateOrZoom = false,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = false,
        )
    ) {
        Marker(state = rememberMarkerState(position = cameraPositionState.position.target))
    }
}

@Composable
private fun AddReview() {
    var isPlaceAlreadyReviewedByUser by remember { mutableStateOf(false) }
    AnimatedVisibility(visible = !isPlaceAlreadyReviewedByUser) {
        Column {
            Text(
                text = "Aporta tu reseña",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = Spacing_06, vertical = Spacing_04),
            )

            var addingReview by rememberSaveable { mutableStateOf(false) }
            var rating by rememberSaveable { mutableStateOf(0) }
            InteractiveRatingBar(
                modifier = Modifier.padding(horizontal = Spacing_06),
                value = rating,
                onValueChange = { addingReview = true; rating = it }
            )

            AnimatedVisibility(visible = addingReview) {
                var isDiscardingReview by rememberSaveable { mutableStateOf(false) }
                AddReviewPost(
                    rating = rating,
                    showDiscardReviewDialog = { isDiscardingReview = true },
                    submitReview = { isPlaceAlreadyReviewedByUser = true },
                )
                if (isDiscardingReview) {
                    DiscardReviewDialog(
                        onDismissRequest = { isDiscardingReview = false },
                        onConfirmClick = {
                            rating = 0
                            addingReview = false
                            isDiscardingReview = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Review(review: Review) {
    val header = HeaderData(
        imageRes = org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant,
        title = {
            Column {
                Text(
                    text = review.user,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                RatingBar(rating = review.rating)
            }
        },
        actions = {
            val icon = if (review.rating == 4) {
                VUIcons.Delete
            } else {
                VUIcons.Report
            }
            VUIcon(
                icon = icon,
                contentDescription = "",
                onIconClick = {},
            )
        }
    )
    GenericPost(
        modifier = Modifier.padding(horizontal = Spacing_06),
        headerData = header,
        content = {
            Text(text = review.title, fontWeight = FontWeight.SemiBold)
            Text(text = review.description, style = MaterialTheme.typography.bodyMedium)
        },
    )
}

data class Review(
    val user: String,
    val rating: Int,
    val title: String,
    val description: String,
)

val review1 = Review(
    user = "Dani.ella",
    rating = 4,
    title = "Tienen un montón de opciones veganas",
    description = "Me pedí una hamburguesa notco con cheddar y estaba riquisima. Vi que en el menú tenían otros sanguches y wraps, además de alguna ensalada. La verdad 100% recomendable!!!"
)

val review2 = Review(
    user = "Pepe Argento",
    rating = 2,
    title = "La mesa se movía",
    description = "La comida estaba buena, el choripán vegano muy rico, pero la mesa se movía para todos lados, unas ganas de fajar a mi hijo."
)

private val reviews = listOf(
    review1,
    review2,
    review2,
    review2,
)

private const val description =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec condimentum nulla in odio tincidunt, ut blandit tellus semper. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec condimentum nulla in odio tincidunt, ut blandit tellus semper. Lorem ipsum dolor sit amet, consectetur adipiscing elit."