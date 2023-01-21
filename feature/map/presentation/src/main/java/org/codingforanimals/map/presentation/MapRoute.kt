package org.codingforanimals.map.presentation

import android.Manifest
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.launch
import org.codingforanimals.map.presentation.MapViewModel.MapUiState
import org.koin.androidx.compose.koinViewModel

private const val TAG = "MapRoute.kt"

@Composable
internal fun MapRoute(
    navigateToSite: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: MapViewModel = koinViewModel(),
) {
    val uiState: MapUiState by viewModel.uiState.collectAsStateWithLifecycle()

    MapScreen(
        uiState = uiState,
        navigateToSite = navigateToSite,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun MapScreen(
    uiState: MapUiState,
    snackbarHostState: SnackbarHostState,
    navigateToSite: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Box {
        Map(navigateToSite)

        PermissionDialog(
            context = context,
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            permissionRationale = "permition rationale!!!",
            snackbarHostState = snackbarHostState,
            permissionAction = {
                when (it) {
                    PermissionAction.Denied -> {
                        Log.e(TAG, "PERMISSION DENIED")
                    }
                    PermissionAction.Granted -> {
                        Log.e(TAG, "PERMISSION GRANTED")
                        scope.launch {
                            snackbarHostState.showSnackbar("Location granted!!")
                        }
                    }
                }
            },
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