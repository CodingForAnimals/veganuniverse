package org.codingforanimals.veganuniverse.places.presentation.home.bottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.places.presentation.home.PlacesHomeViewModel
import org.codingforanimals.veganuniverse.places.presentation.home.PlacesHomeViewModel.Action
import org.codingforanimals.veganuniverse.places.presentation.home.state.PlacesState
import org.codingforanimals.veganuniverse.places.presentation.home.state.UserLocationState
import org.codingforanimals.veganuniverse.places.presentation.utils.mapStyleJson


@Composable
internal fun PlacesHomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: PlacesHomeViewModel.UiState,
    onMapLoaded: () -> Unit,
    onAction: (Action) -> Unit,
) = with(uiState) {
    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = userLocationState is UserLocationState.Success,
                mapStyleOptions = MapStyleOptions(mapStyleJson),
                latLngBoundsForCameraTarget = LatLngBounds(
                    LatLng(-55.0, -74.0),
                    LatLng(-22.0, -53.0),
                ),
                minZoomPreference = 5f
            ),
            onMapLoaded = onMapLoaded,
            uiSettings = MapUiSettings(
                compassEnabled = userMapControlEnabled,
                indoorLevelPickerEnabled = userMapControlEnabled,
                mapToolbarEnabled = userMapControlEnabled,
                myLocationButtonEnabled = userMapControlEnabled,
                rotationGesturesEnabled = userMapControlEnabled,
                scrollGesturesEnabled = userMapControlEnabled,
                scrollGesturesEnabledDuringRotateOrZoom = userMapControlEnabled,
                tiltGesturesEnabled = userMapControlEnabled,
                zoomControlsEnabled = userMapControlEnabled,
                zoomGesturesEnabled = userMapControlEnabled
            ),
            onMapClick = { onAction(Action.OnMapClick) },
            onPOIClick = { onAction(Action.OnMapClick) },
            onMyLocationClick = { onAction(Action.OnMapClick) },
            content = {
                when (placesState) {
                    PlacesState.Error, PlacesState.Loading -> Unit
                    is PlacesState.Success -> {
                        for (entity in placesState.content) {
                            key(entity.geoHash) {
                                val selected = isPlaceSelected(entity)
                                MarkerInfoWindow(
                                    state = entity.state,
                                    icon = entity.marker.getDisplayMarker(isSelected = selected),
                                    onClick = {
                                        it.showInfoWindow()
                                        onAction(Action.OnPlaceClick(entity))
                                        true
                                    },
                                    content = {
                                        OutlinedCard(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                                contentColor = MaterialTheme.colorScheme.primary,
                                            ),
                                            border = BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.primary
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
                                        entity.state.showInfoWindow()
                                    } else {
                                        entity.state.hideInfoWindow()
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
            CircularProgressIndicator()
        }

        AnimatedVisibility(
            visible = isRefreshButtonVisible,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = Spacing_02),
            content = {
                Button(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = Spacing_02),
                    onClick = { onAction(Action.OnRefreshPlacesButtonClick) },
                    content = { Text(text = "Buscar en esta zona") },
                )
            }
        )
//
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
