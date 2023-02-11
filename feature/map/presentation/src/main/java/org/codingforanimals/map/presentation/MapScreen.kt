package org.codingforanimals.map.presentation

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.map.presentation.mockdata.Site
import org.codingforanimals.map.presentation.mockdata.sites
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    snackbarHostState: SnackbarHostState,
    cameraPositionState: CameraPositionState,
    navigateToSite: () -> Unit,
    viewModel: MapViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    RequestLocationPermission(
        locationGranted = uiState.locationGranted,
        snackbarHostState = snackbarHostState,
        onLocationPermissionGranted = { viewModel.onAction(MapViewModel.Action.OnLocationGranted) },
    )

    MapScreen(
        modifier = Modifier.fillMaxSize(),
        onAction = viewModel::onAction,
        cameraPositionState = cameraPositionState,
        uiState = uiState,
        navigateToSite = navigateToSite,
    )

}

@Composable
private fun MapScreen(
    modifier: Modifier = Modifier,
    onAction: (MapViewModel.Action) -> Unit,
    cameraPositionState: CameraPositionState,
    uiState: MapViewModel.UiState,
    navigateToSite: () -> Unit,
) = with(uiState) {
    Box(modifier = modifier) {
        Map(
            modifier = modifier,
            locationGranted = locationGranted,
            cameraPositionState = cameraPositionState,
            onMapClick = { onAction(MapViewModel.Action.OnCardClose) }
        ) {
            Markers { site -> onAction(MapViewModel.Action.OnMarkerClicked(site)) }
            SelectedMarker(showCard = showCard, selectedSite = selectedSite)
        }
        SiteCard(site = selectedSite, visible = showCard, navigateToSite = navigateToSite)
    }
}

@Composable
private fun Map(
    modifier: Modifier = Modifier,
    locationGranted: Boolean = false,
    zoomControlsEnabled: Boolean = false,
    mapToolbarEnabled: Boolean = false,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapClick: (LatLng) -> Unit,
    content: @Composable @GoogleMapComposable (() -> Unit)?,
) {
    val properties = MapProperties(isMyLocationEnabled = locationGranted)
    val uiSettings = MapUiSettings(
        zoomControlsEnabled = zoomControlsEnabled,
        mapToolbarEnabled = mapToolbarEnabled
    )
    GoogleMap(
        modifier = modifier,
        properties = properties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState,
        onMapClick = onMapClick,
        content = content,
    )
}

@Composable
private fun SelectedMarker(
    showCard: Boolean,
    selectedSite: Site?
) {
    if (showCard && selectedSite != null)
        Marker(
            state = MarkerState(selectedSite.latLng),
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
        )
}

@Composable
private fun BoxScope.SiteCard(
    site: Site?,
    visible: Boolean,
    navigateToSite: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .align(Alignment.BottomCenter),
        visible = visible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        ElevatedCard(
            modifier = Modifier
                .clickable { navigateToSite() }
                .padding(18.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = site?.name ?: "")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AsyncImage(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        model = R.drawable.vegan_restaurant,
                        contentDescription = "",
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Martínez 5434")

                        Row {
                            repeat(3) {
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                            repeat(2) {
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = "",
                                    tint = Color.LightGray,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Markers(
    onClick: (Site) -> Unit,
) {
    sites.forEach { site ->
        Marker(
            state = MarkerState(position = site.latLng),
            onClick = { onClick(site); false }
        )
    }
}

@Composable
private fun RequestLocationPermission(
    locationGranted: Boolean,
    snackbarHostState: SnackbarHostState,
    onLocationPermissionGranted: () -> Unit,
) {
    if (locationGranted) return
    val context = LocalContext.current
    PermissionDialog(
        context = context,
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        permissionRationale = "Con tu ubicación podemos personalizar la sección Lugares especialmente para vos",
        snackbarHostState = snackbarHostState,
        permissionAction = {
            when (it) {
                PermissionAction.Denied -> Unit
                PermissionAction.Granted -> onLocationPermissionGranted()
            }
        },
    )
}
