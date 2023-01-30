@file:OptIn(ExperimentalComposeUiApi::class)

package org.codingforanimals.places.presentation

import android.Manifest
import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant
import org.codingforanimals.veganuniverse.core.ui.components.Bold
import org.codingforanimals.veganuniverse.core.ui.components.ImageAndTextCard
import org.codingforanimals.veganuniverse.core.ui.permissions.PermissionAction
import org.codingforanimals.veganuniverse.core.ui.permissions.PermissionDialog
import org.koin.androidx.compose.koinViewModel

private val padding = 20.dp

@Composable
private fun HandleSideEffects(
    sideEffect: Flow<PlacesViewModel.SideEffect>,
    cameraPositionState: CameraPositionState,
) {
    LaunchedEffect(Unit) {
        sideEffect.onEach { effect ->
            when (effect) {
                is PlacesViewModel.SideEffect.ZoomOnUserLocation -> {
                    animateToLocation(cameraPositionState, effect.latLng)
                }
            }
        }.collect()
    }
}

@Composable
fun PlacesScreen(
    snackbarHostState: SnackbarHostState,
    cameraPositionState: CameraPositionState,
    navigateToMap: (Double, Double, Float) -> Unit,
    viewModel: PlacesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    var columnScrollEnabled by remember { mutableStateOf(true) }

    HandleSideEffects(
        sideEffect = viewModel.sideEffect,
        cameraPositionState = cameraPositionState,
    )

    RequestLocationPermission(
        locationGranted = uiState.locationGranted,
        snackbarHostState = snackbarHostState,
        onLocationPermissionGranted = { viewModel.onAction(PlacesViewModel.UserAction.OnLocationGranted) },
    )

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            columnScrollEnabled = true
        }
    }

    LazyColumn(userScrollEnabled = columnScrollEnabled) {
        item {
            InOrbit()
        }
        item {
            NearbyPlaces(
                cameraPositionState = cameraPositionState,
                showUserLocation = uiState.locationGranted,
                navigateToMap = navigateToMap,
                onMapTouched = { columnScrollEnabled = false }
            )
        }
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

@Composable
private fun InOrbit() {
    Bold(
        modifier = Modifier.padding(top = padding, bottom = padding, start = padding),
        text = "En órbita",
    )

    LazyRow(
        contentPadding = PaddingValues(padding, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(padding)
    ) {
        items(10) {
            ImageAndTextCard(
                imageId = vegan_restaurant,
                text = "Todo vegano",
                onClick = {}
            )
        }
    }
}

@Composable
private fun NearbyPlaces(
    cameraPositionState: CameraPositionState,
    showUserLocation: Boolean,
    onMapTouched: () -> Unit,
    navigateToMap: (Double, Double, Float) -> Unit,
) {
    Column {
        NearbyPlacesTitle {
            with(cameraPositionState.position) {
                navigateToMap(target.latitude, target.longitude, zoom)
            }
        }

        Map(
            cameraPositionState = cameraPositionState,
            showUserLocation = showUserLocation,
            onMapTouched = onMapTouched,
        )

        NearbyPlacesItems()
    }

}

@Composable
private fun NearbyPlacesItems() {

}

@Composable
private fun NearbyPlacesTitle(navigateToMap: () -> Unit) {
    Row(
        modifier = Modifier.padding(top = padding, start = padding),
        horizontalArrangement = Arrangement.spacedBy(padding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Bold(
            text = "Lugares cercanos",
        )
        TextButton(onClick = navigateToMap) {
            Text(text = "Pantalla completa")
        }
    }
}

@Composable
private fun Map(
    cameraPositionState: CameraPositionState,
    showUserLocation: Boolean,
    onMapTouched: () -> Unit,
) {
    GoogleMap(
        modifier = Modifier
            .padding(horizontal = padding)
            .aspectRatio(1.75f)
            .pointerInteropFilter(
                onTouchEvent = {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            onMapTouched()
                            false
                        }
                        else -> {
                            true
                        }
                    }
                }
            ),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(mapToolbarEnabled = false, zoomControlsEnabled = false),
        properties = MapProperties(isMyLocationEnabled = showUserLocation)
    )
}


















