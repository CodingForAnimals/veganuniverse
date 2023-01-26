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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.map.presentation.mockdata.Site
import org.codingforanimals.map.presentation.mockdata.sites
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    snackbarHostState: SnackbarHostState,
    navigateToSite: () -> Unit,
    viewModel: MapViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    HandleSideEffects(
        effectsFlow = viewModel.sideEffect,
        cameraPositionState = cameraPositionState,
    )

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
    val uiSettings = MapUiSettings(zoomControlsEnabled = zoomControlsEnabled, mapToolbarEnabled = mapToolbarEnabled)
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
        println("composing site marker ${site.name}")
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
    println("recomposition request location permission")
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
private fun HandleSideEffects(
    effectsFlow: Flow<MapViewModel.SideEffect>,
    cameraPositionState: CameraPositionState,
) {
    LaunchedEffect(Unit) {
        effectsFlow.onEach { effect ->
            when (effect) {
                is MapViewModel.SideEffect.UserLocationZoom -> {
                    cameraPositionState.animate(
                        durationMs = 1_000,
                        update = CameraUpdateFactory.newCameraPosition(
                            CameraPosition(effect.latLng, 16f, 0f, 0f)
                        ),
                    )
                }
            }
        }.collect()
    }
}


//@Composable
//internal fun MapRoute(
//    snackbarHostState: SnackbarHostState,
//    viewModel: MapViewModel = koinViewModel(),
//) {
//
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(viewModel.defaultCameraPosition, 4f)
//    }
//
//    val state = viewModel.uiState.value
//    LaunchedEffect(Unit) {
//        viewModel.effect.onEach { effect ->
//            when (effect) {
//                is MapUiContract.Effect.LocateUserInMap -> {
//                    cameraPositionState.animate(
//                        durationMs = 1_000,
//                        update = CameraUpdateFactory.newCameraPosition(
//                            CameraPosition(effect.userCurrentLocation, 16f, 0f, 0f)
//                        ),
//                    )
//                }
//            }
//        }.collect()
//    }
//
//    println("pepe argento locationGranted ${state.locationGranted}")
//    if (!state.locationGranted) {
//        RequestLocationPermission(
//            snackbarHostState = snackbarHostState,
//            onLocationPermissionGranted = {
//                println("pepe argento send location granted event")
//                viewModel.setEvent(MapUiContract.Event.LocationGranted)
//            },
//        )
//    }
//
//    MapScreen(
//        isLocationGranted = state.locationGranted,
//        cameraPositionState = cameraPositionState,
//        onEvent = { event -> viewModel.setEvent(event) },
//        showCard = state.showCard,
//        selectedSite = state.selectedSite,
//    )
//
//    AnimatedVisibility(
//        visible = state.showBriefing,
//        enter = slideInVertically { it },
//        exit = slideOutVertically { it },
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White),
//        ) {
//            Text(text = "This is site ${state.selectedSite?.name}")
//        }
//    }
//
//    BackHandler(state.showBriefing) {
//        viewModel.setEvent(MapUiContract.Event.onBriefingClose)
//    }
//}
//
//
//@Composable
//private fun MapScreen(
//    isLocationGranted: Boolean,
//    cameraPositionState: CameraPositionState,
//    onEvent: (MapUiContract.Event) -> Unit,
//    showCard: Boolean,
//    selectedSite: Site?,
//) {
//    Box {
//        Map(
//            isLocationGranted = isLocationGranted,
//            cameraPositionState = cameraPositionState,
//            onEvent = onEvent,
//            showCard = showCard,
//            selectedSite = selectedSite,
//        )
//    }
//}
//
//@Composable
//private fun BoxScope.Map(
//    isLocationGranted: Boolean,
//    cameraPositionState: CameraPositionState,
//    showCard: Boolean,
//    selectedSite: Site?,
//    onEvent: (MapUiContract.Event) -> Unit,
//) {
//    GoogleMap(
//        modifier = Modifier.fillMaxSize(),
//        cameraPositionState = cameraPositionState,
//        properties = MapProperties(isMyLocationEnabled = isLocationGranted),
//        uiSettings = MapUiSettings(
//            mapToolbarEnabled = false,
//            zoomControlsEnabled = false,
//        ),
//        onMapClick = { onEvent(MapUiContract.Event.onCardClose) }
//    ) {
//        Places { site -> onEvent(MapUiContract.Event.OnMarkerClick(site)) }
//
//        if (showCard && selectedSite != null) {
//            Marker(
//                state = MarkerState(selectedSite.latLng),
//                icon = BitmapDescriptorFactory.defaultMarker(HUE_GREEN),
//            )
//        }
//    }
//
//    AnimatedVisibility(
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight(0.4f)
//            .align(Alignment.BottomCenter),
//        visible = showCard,
//        enter = slideInVertically { it },
//        exit = slideOutVertically { it }
//    ) {
//        ElevatedCard(
//            modifier = Modifier
//                .padding(18.dp)
//                .clickable { onEvent(MapUiContract.Event.OnCardClick) },
//            colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
//        ) {
//            Column(
//                modifier = Modifier.padding(20.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//            ) {
//                Text(text = selectedSite?.name ?: "")
//                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                    AsyncImage(
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxSize()
//                            .clip(RoundedCornerShape(8.dp)),
//                        contentScale = ContentScale.Crop,
//                        model = R.drawable.vegan_restaurant,
//                        contentDescription = "",
//                    )
//                    Column(modifier = Modifier.weight(1f)) {
//                        Text(text = "Martínez 5434")
//
//                        Row {
//                            repeat(3) {
//                                Icon(
//                                    imageVector = Icons.Rounded.Star,
//                                    contentDescription = "",
//                                    tint = MaterialTheme.colorScheme.primary,
//                                )
//                            }
//                            repeat(2) {
//                                Icon(
//                                    imageVector = Icons.Rounded.Star,
//                                    contentDescription = "",
//                                    tint = Color.LightGray,
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
