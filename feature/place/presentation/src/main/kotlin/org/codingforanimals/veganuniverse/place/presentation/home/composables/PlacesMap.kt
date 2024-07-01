package org.codingforanimals.veganuniverse.place.presentation.home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindow
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel.Action
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel.PlacesState
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeViewModel.UserLocationState
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
internal fun PlacesMap(
    modifier: Modifier = Modifier,
    placesState: PlacesState,
    uiState: PlacesHomeViewModel.UiState,
    userLocationState: UserLocationState,
    onAction: (Action) -> Unit,
) = with(uiState) {
    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = userLocationState is UserLocationState.Success,
                mapStyleOptions = MapStyleOptions(org.codingforanimals.veganuniverse.place.presentation.utils.mapStyleJson),
                latLngBoundsForCameraTarget = LatLngBounds(
                    LatLng(-55.0, -74.0),
                    LatLng(-22.0, -53.0),
                ),
                minZoomPreference = 5f,
            ),
            onMapClick = { onAction(Action.OnMapClick) },
            onPOIClick = { onAction(Action.OnMapClick) },
            onMyLocationClick = { onAction(Action.OnMapClick) },
            content = {
                when (placesState) {
                    PlacesState.Error, PlacesState.Loading -> Unit
                    is PlacesState.Success -> {
                        placesState.filteredPlaces.forEach { entity ->
                            key(entity.geoHash) {
                                val selected = remember(selectedPlace, isFocused) {
                                    isPlaceSelected(entity)
                                }
                                val markerIcon = org.codingforanimals.veganuniverse.place.presentation.home.model.PlaceMarker.getMarker(entity.type)
                                    .getDisplayMarker(selected)
                                    ?.let { BitmapDescriptorFactory.fromBitmap(it) }

                                MarkerInfoWindow(
                                    state = entity.markerState,
                                    icon = markerIcon,
                                    onClick = {
//                                        it.showInfoWindow()
                                        onAction(Action.OnPlaceClick(entity))
//                                        true
                                        false
                                    },
                                    content = {
                                        OutlinedCard(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                                contentColor = MaterialTheme.colorScheme.primary,
                                            ),
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(
                                                    horizontal = Spacing_04,
                                                    vertical = Spacing_02
                                                ),
                                                text = entity.name,
                                                fontWeight = FontWeight.SemiBold,
                                            )
                                        }
                                    }
                                )
                                LaunchedEffect(selected) {
                                    if (selected) {
                                        entity.markerState.showInfoWindow()
                                    } else {
                                        entity.markerState.hideInfoWindow()
                                    }
                                }
                            }
                        }
                    }
                }
            },
        )

        AnimatedVisibility(
            visible = userLocationState is UserLocationState.Loading,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)
                .padding(18.dp)
                .size(24.dp),
        ) {
            CircularProgressIndicator(strokeWidth = 3.dp)
        }

        Row(
            modifier = Modifier
                .padding(top = Spacing_02)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing_02),
        ) {
            Button(
                onClick = { onAction(Action.OnRefreshPlacesButtonClick) },
                content = { Text(text = "Buscar en esta zona") },
            )
            FilledIconButton(
                onClick = { onAction(Action.OnOpenSearchCityGoogleMapsOverlay) },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                VUIcon(
                    icon = VUIcons.Search,
                    contentDescription = "",
                )
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Spacing_03)
                .shadow(5.dp, CircleShape),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            onClick = { onAction(Action.OnExpandSheetButtonClick) },
            content = { VUIcon(icon = VUIcons.ArrowUpward, contentDescription = "") },
        )
    }
}
