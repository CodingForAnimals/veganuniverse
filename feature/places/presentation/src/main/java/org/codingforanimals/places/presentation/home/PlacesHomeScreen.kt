@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package org.codingforanimals.places.presentation.home

import android.Manifest
import android.view.MotionEvent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.places.presentation.animateToLocation
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.permissions.PermissionAction
import org.codingforanimals.veganuniverse.core.ui.permissions.PermissionDialog
import org.codingforanimals.veganuniverse.core.ui.theme.PrimaryLight
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlacesHomeScreen(
    snackbarHostState: SnackbarHostState,
    cameraPositionState: CameraPositionState,
    onPlaceClick: () -> Unit,
    viewModel: PlacesHomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    HandleSideEffects(
        sideEffect = viewModel.sideEffect,
        cameraPositionState = cameraPositionState,
    )

    RequestLocationPermission(
        locationGranted = uiState.locationGranted,
        snackbarHostState = snackbarHostState,
        onLocationPermissionGranted = { viewModel.onAction(PlacesHomeViewModel.UserAction.OnLocationGranted) },
    )

    PlacesHomeScreen(
        cameraPositionState = cameraPositionState,
        uiState = uiState,
        onPlaceClick = onPlaceClick,
    )
}

@Composable
private fun PlacesHomeScreen(
    cameraPositionState: CameraPositionState,
    uiState: PlacesHomeViewModel.UiState,
    onPlaceClick: () -> Unit,
) {
    var columnScrollEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(cameraPositionState.isMoving, columnScrollEnabled) {
        if (!cameraPositionState.isMoving) {
            columnScrollEnabled = true
        }
    }

    LazyColumn(userScrollEnabled = columnScrollEnabled) {
        item {
            Map(
                cameraPositionState = cameraPositionState,
                isLocationGranted = uiState.locationGranted,
                onMapTouched = { columnScrollEnabled = false },
                onMapReleased = { columnScrollEnabled = true },
            )
        }
        item {
            NearbyPlaces(
                onPlaceClick = onPlaceClick,
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
private fun Map(
    cameraPositionState: CameraPositionState,
    isLocationGranted: Boolean,
    onMapTouched: () -> Unit,
    onMapReleased: () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val aspectRatio by animateFloatAsState(
        if (expanded) 1f else 1.6f
    )
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        GoogleMap(
            modifier = Modifier
                .aspectRatio(aspectRatio)
                .pointerInteropFilter(
                    onTouchEvent = {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                onMapTouched()
                                false
                            }
                            MotionEvent.ACTION_UP -> {
                                onMapReleased()
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
            properties = MapProperties(isMyLocationEnabled = isLocationGranted),
        )
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(all = Spacing_04),
            onClick = { expanded = !expanded },
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            val icon = if (expanded) VUIcons.Shrink else VUIcons.Expand
            Crossfade(targetState = icon) {
                VUIcon(icon = it, contentDescription = "")
            }
        }
    }
}

@Composable
private fun NearbyPlaces(
    onPlaceClick: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(top = Spacing_04, end = Spacing_04, start = Spacing_04),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        VUAssistChip(
            icon = VUIcons.Filter,
            label = "Filtrar",
            onClick = {},
            iconDescription = ""
        )
        VUAssistChip(
            icon = VUIcons.Sort,
            label = "Ordenar",
            onClick = {},
            iconDescription = ""
        )
    }

    val nearbyPlaces = listOf(
        "Todo Vegano",
        "El Café de Noah",
        "Lado V",
        "Vegan Fox",
        "La Reverde",
        "Loving Hut",
        "Estilo Veggie",
    )

    nearbyPlaces.forEachIndexed { index, name ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(all = Spacing_04)
                .clickable(onClick = onPlaceClick),
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
                            val icon = if (index % 2 == 0) VUIcons.Store else VUIcons.Utensils
                            VUIcon(icon = icon, contentDescription = "")
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                        RatingBar(
                            rating = 4.5f,
                            color = PrimaryLight,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing_02),
                    ) {
                        VUIcon(icon = VUIcons.Places, contentDescription = "")
                        Text(
                            text = "Bmé Mitre 695", fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Text(
                        text = "Monte Grande, Buenos Aires",
                        style = MaterialTheme.typography.bodyMedium,
                    )
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
}

@Composable
private fun HandleSideEffects(
    sideEffect: Flow<PlacesHomeViewModel.SideEffect>,
    cameraPositionState: CameraPositionState,
) {
    LaunchedEffect(Unit) {
        sideEffect.onEach { effect ->
            when (effect) {
                is PlacesHomeViewModel.SideEffect.ZoomOnUserLocation -> {
                    animateToLocation(cameraPositionState, effect.latLng)
                }
            }
        }.collect()
    }
}
