@file:OptIn(ExperimentalComposeUiApi::class)

package org.codingforanimals.places.presentation

import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant
import org.codingforanimals.veganuniverse.core.ui.components.Bold
import org.codingforanimals.veganuniverse.core.ui.components.ImageAndTextCard

private val padding = 20.dp

@Composable
fun PlacesScreen(
    cameraPositionState: CameraPositionState,
    navigateToMap: () -> Unit,
) {
    var columnScrollEnabled by remember { mutableStateOf(true) }

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
                navigateToMap = navigateToMap,
                onMapTouched = { columnScrollEnabled = false }
            )
        }
    }
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
    onMapTouched: () -> Unit,
    navigateToMap: () -> Unit,
) {
    Column {
        NearbyPlacesTitle(navigateToMap)

        Map(
            cameraPositionState = cameraPositionState,
            onMapTouched = onMapTouched,
        )
    }
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
    onMapTouched: () -> Unit,
) {
    // TODO
    // que en Places (o sea acá), el usuario al ingresar a la pantalla
    // sea pedido su ubicación, como pasa acutalmente en Map.
    // si no hay permiso, mostrar por defecto argentina
    // si hay permiso, llevar al usuario a la ubicación actual.
    // cuando el usuario abra el mapa con pantalla completa
    // mandar por argumento la ubicación actual DEL MAPA, no del usuario
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
        uiSettings = MapUiSettings(mapToolbarEnabled = false, zoomControlsEnabled = false)
    )
}


















