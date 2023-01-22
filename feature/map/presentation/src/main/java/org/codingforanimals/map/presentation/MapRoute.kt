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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.codingforanimals.map.presentation.mockdata.Place
import org.codingforanimals.map.presentation.mockdata.places
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MapRoute(
    navigateToSite: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: MapViewModel = koinViewModel(),
) {
    val mapUiState by viewModel.mapUiState.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapUiState.initialCameraPosition, 4f)
    }

    if (mapUiState.isUserLocationEnabled) {
        LaunchedEffect(Unit) {
            cameraPositionState.animate(
                durationMs = 3_000,
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(mapUiState.initialCameraPosition, 16f, 0f, 0f)
                ),
            )
        }
    } else {
        RequestLocationPermission(
            snackbarHostState = snackbarHostState,
            onLocationPermissionGranted = viewModel::fetchUserLocation,
        )
    }

    MapScreen(
        isMyLocationEnabled = mapUiState.isUserLocationEnabled,
        cameraPositionState = cameraPositionState,
        navigateToSite = navigateToSite,
    )
}

@Composable
private fun RequestLocationPermission(
    snackbarHostState: SnackbarHostState,
    onLocationPermissionGranted: () -> Unit,
) {
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

@Composable
private fun MapScreen(
    isMyLocationEnabled: Boolean,
    cameraPositionState: CameraPositionState,
    navigateToSite: () -> Unit,
) {
    Box {
        Map(isMyLocationEnabled, cameraPositionState, navigateToSite)
    }
}

@Composable
private fun BoxScope.Map(
    isMyLocationEnabled: Boolean,
    cameraPositionState: CameraPositionState,
    navigateToSite: () -> Unit,
) {
    var selectedPlace by rememberSaveable { mutableStateOf<Place?>(null) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = isMyLocationEnabled),
        uiSettings = MapUiSettings(
            mapToolbarEnabled = false,
            zoomControlsEnabled = false,
        ),
        onMapClick = { selectedPlace = null }
    ) {
        Places { selectedPlace = it }

        selectedPlace?.let { place ->
            Marker(
                state = MarkerState(position = place.latLng),
                icon = BitmapDescriptorFactory.defaultMarker(HUE_GREEN),
            )
        }
    }

    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .align(Alignment.BottomCenter),
        visible = selectedPlace != null,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Card(
            modifier = Modifier
                .padding(18.dp)
                .clickable { navigateToSite() },
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = selectedPlace?.name ?: "")
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
private fun Places(
    onClick: (Place) -> Unit,
) {
    places.forEach { place ->
        println("pepe argento NORMAL $place")
        Marker(
            state = MarkerState(position = place.latLng),
            onClick = { onClick(place); false }
        )
    }
}