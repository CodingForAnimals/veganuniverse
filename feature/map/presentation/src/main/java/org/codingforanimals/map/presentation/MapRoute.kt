package org.codingforanimals.map.presentation

import android.Manifest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.map.presentation.MapViewModel.MapUiState
import org.codingforanimals.map.presentation.MapViewModel.MapUiState.Loading
import org.codingforanimals.map.presentation.MapViewModel.MapUiState.Success
import org.codingforanimals.veganuniverse.core.ui.permissions.RequestPermission
import org.koin.androidx.compose.koinViewModel

private const val TAG = "MapRoute.kt"

@Composable
internal fun MapRoute(
    navigateToSite: () -> Unit,
    viewModel: MapViewModel = koinViewModel(),
) {
    val uiState: MapUiState by viewModel.uiState.collectAsStateWithLifecycle()

    MapScreen(
        uiState = uiState,
        navigateToSite = navigateToSite,
    )
}

@Composable
private fun MapScreen(
    uiState: MapUiState,
    navigateToSite: () -> Unit,
) {

    Box {
        Map(navigateToSite)
        when (uiState) {
            Loading -> {
                Log.i(TAG, "Loading preference")
                Unit
            }
            is Success -> {
                Log.i(TAG, "Preference loaded: ${uiState.locationPermissionsGranted}")
            }
        }
        RequestPermission(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Text(text = "se ha compartido la ubicación")
                }
            }
        )
    }
}

@Composable
private fun BoxScope.Map(
    navigateToSite: () -> Unit,
) {
    var isCardVisible by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = MapUiSettings(
            mapToolbarEnabled = false,
            zoomControlsEnabled = false,
        ),
        onMapClick = { isCardVisible = false }
    ) {
        Marker(
            state = MarkerState(position = LatLng(-50.0, -50.0)),
            title = "Restaurante Pepe Argento",
            icon = if (isCardVisible) BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_GREEN
            ) else BitmapDescriptorFactory.defaultMarker(),
            onClick = {
                isCardVisible = true
                title = it.title ?: "Default"
                false
            },
            onInfoWindowClose = {
                isCardVisible = false
            },
        )
    }

    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .align(Alignment.BottomCenter),
        visible = isCardVisible,
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
                Text(text = title)
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