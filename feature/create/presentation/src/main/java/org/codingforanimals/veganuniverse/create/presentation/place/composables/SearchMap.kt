package org.codingforanimals.veganuniverse.create.presentation.place.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHeroColors
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.UiState


@Composable
internal fun SearchMap(
    uiState: UiState,
    onAction: (Action) -> Unit,
) = with(uiState) {

    val colors = if (location == null) {
        if (form.locationError) {
            ItemDetailHeroColors.errorColors()
        } else {
            ItemDetailHeroColors.secondaryColors()
        }
    } else {
        ItemDetailHeroColors.primaryColors()
    }

    val blur = animateDpAsState(targetValue = if (location == null) 5.dp else 0.dp)
    Box {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .blur(blur.value)
                .aspectRatio(2f)
                .padding(bottom = 20.dp)
                .clickable { onAction(Action.OnSearchMapClick) },
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            content = {
                if (location == null) return@GoogleMap
                val markerState = remember(location.latLng) { MarkerState(location.latLng) }
                Marker(markerState)
            }
        )

        Crossfade(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
                .padding(bottom = 20.dp)
                .clickable { onAction(Action.OnSearchMapClick) },
            targetState = location == null,
        ) {
            if (it) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xADA1A1AA)),
                    contentAlignment = Alignment.Center,
                ) {
                    OutlinedButton(
                        onClick = { onAction(Action.OnSearchMapClick) },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        VUIcon(icon = VUIcons.Search, contentDescription = "")
                        Text(
                            modifier = Modifier.padding(start = Spacing_02),
                            text = "Buscar por nombre o dirección",
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onAction(Action.OnSearchMapClick) }
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        if (location?.address?.isNotBlank() == true) {
                            OutlinedButton(
                                modifier = Modifier.align(Alignment.Center),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                onClick = { onAction(Action.OnSearchMapClick) },
                            ) {
                                Text(text = location.address)
                            }
                        }
                    }
                }
            }
        }
//        AnimatedVisibility(
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(2f)
//                .padding(bottom = 20.dp)
//                .clickable { onAction(Action.OnSearchMapClick) },
//            visible = form.location == null,
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color(0xADA1A1AA)),
//                contentAlignment = Alignment.Center,
//            ) {
//                OutlinedButton(
//                    onClick = { onAction(Action.OnSearchMapClick) },
//                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
//                ) {
//                    VUIcon(icon = VUIcons.Search, contentDescription = "")
//                    Text(
//                        modifier = Modifier.padding(start = Spacing_02),
//                        text = "Buscar por nombre o dirección",
//                    )
//                }
//            }
//        }
        BottomDivider(
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.BottomCenter),
            colors = colors,
        )
    }
}

@Composable
private fun BottomDivider(
    modifier: Modifier = Modifier,
    colors: ItemDetailHeroColors,
) {
    Box(
        modifier = modifier,
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.Center)
                .height(4.dp)
                .fillMaxWidth()
                .background(colors.divider)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = Spacing_06),
        ) {
            Box {
                Canvas(
                    modifier = Modifier.size(30.dp),
                    onDraw = {
                        drawCircle(
                            radius = 20.dp.toPx(),
                            color = colors.iconContainer,
                        )
                        drawCircle(
                            radius = 20.dp.toPx(),
                            color = colors.typeIconBorder,
                            style = Stroke(3.dp.toPx())
                        )
                    },
                )
                VUIcon(
                    modifier = Modifier.align(Alignment.Center),
                    icon = VUIcons.LocationFilled,
                    contentDescription = "",
                    tint = colors.typeIconTint,
                )
            }
        }
    }
}

private val mapUiSettings = MapUiSettings(
    compassEnabled = false,
    indoorLevelPickerEnabled = false,
    mapToolbarEnabled = false,
    myLocationButtonEnabled = false,
    rotationGesturesEnabled = false,
    scrollGesturesEnabled = false,
    scrollGesturesEnabledDuringRotateOrZoom = false,
    tiltGesturesEnabled = false,
    zoomControlsEnabled = false,
    zoomGesturesEnabled = false
)