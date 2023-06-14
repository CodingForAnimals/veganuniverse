package org.codingforanimals.veganuniverse.create.presentation.place.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.common.AddressField
import org.codingforanimals.veganuniverse.create.presentation.common.LocationField
import org.codingforanimals.veganuniverse.create.presentation.common.TypeField
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.place.VUTextField

@Composable
internal fun SearchMap(
    uiState: CreatePlaceViewModel.UiState,
    onAction: (Action) -> Unit,
) = with(uiState) {

    val colors = if (locationField.isValid) {
        SearchMapDefaults.successColors()
    } else {
        if (isValidating) {
            SearchMapDefaults.errorColors()
        } else {
            SearchMapDefaults.defaultColors()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
            Map(
                cameraPositionState = cameraPositionState,
                location = locationField,
                typeField = typeField,
                onAction = onAction,
            )
            Foreground(
                addressField = addressField,
                isValidating = uiState.isValidating,
                onAction = onAction,
                colors = colors,
            )
        }
        BottomDivider(
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.BottomCenter),
            colors = colors,
        )
    }
}

@Composable
private fun Map(
    cameraPositionState: CameraPositionState,
    location: LocationField,
    typeField: TypeField,
    onAction: (Action) -> Unit,
) {
    val blur = animateDpAsState(targetValue = if (!location.isValid) 5.dp else 0.dp)
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .blur(blur.value)
            .clickable { onAction(Action.OnSearchMapClick) },
        onMapLoaded = { },
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        content = {
            if (location.latLng == null) return@GoogleMap
            val icon = key(typeField.marker) { typeField.getIcon() }
            val markerState = remember(location.latLng) { MarkerState(location.latLng) }
            Marker(
                state = markerState,
                icon = icon,
            )

        }
    )
}

@Composable
private fun Foreground(
    addressField: AddressField,
    isValidating: Boolean,
    onAction: (Action) -> Unit,
    colors: SearchMapColors,
) {
    Crossfade(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onAction(Action.OnSearchMapClick) },
        targetState = addressField.streetAddress == null,
    ) { addressIsNull ->
        if (addressIsNull) {
            SearchPlaceForeground(onAction, colors)
        } else {
            PlaceLocationForeground(addressField, isValidating, onAction)
        }
    }
}

@Composable
private fun SearchPlaceForeground(
    onAction: (Action) -> Unit,
    colors: SearchMapColors,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        OutlinedButton(
            onClick = { onAction(Action.OnSearchMapClick) },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = BorderStroke(1.dp, colors.buttonContentColor),
        ) {
            VUIcon(icon = VUIcons.Search, contentDescription = "", tint = colors.buttonContentColor)
            Text(
                modifier = Modifier.padding(start = Spacing_03),
                text = stringResource(R.string.place_search_map_prompt_button_label),
                color = colors.buttonContentColor,
            )
        }
    }
}

@Composable
private fun PlaceLocationForeground(
    addressField: AddressField,
    isValidating: Boolean,
    onAction: (Action) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        VUTextField(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing_06),
            value = addressField.streetAddress ?: "",
            onValueChange = { onAction(Action.OnFormChange(address = it)) },
            isError = isValidating && !addressField.isValid,
            leadingIcon = VUIcons.Location,
            maxLines = 2,
        )
    }
}

@Composable
private fun BottomDivider(
    modifier: Modifier = Modifier,
    colors: SearchMapColors,
) {
    Box(
        modifier = modifier,
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.Center)
                .height(4.dp)
                .fillMaxWidth()
                .background(colors.dividerColor)
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
                            color = colors.dividerColor,
                        )
                        drawCircle(
                            radius = 20.dp.toPx(),
                            color = colors.iconBorderColor,
                            style = Stroke(3.dp.toPx())
                        )
                    },
                )
                VUIcon(
                    modifier = Modifier.align(Alignment.Center),
                    icon = VUIcons.LocationFilled,
                    contentDescription = "",
                    tint = colors.iconTintColor,
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

private data class SearchMapColors(
    val buttonContentColor: Color,
    val dividerColor: Color,
    val iconContainerColor: Color,
    val iconTintColor: Color,
    val iconBorderColor: Color,
)

private object SearchMapDefaults {
    @Composable
    fun defaultColors() = SearchMapColors(
        buttonContentColor = MaterialTheme.colorScheme.primary,
        dividerColor = MaterialTheme.colorScheme.outline,
        iconContainerColor = MaterialTheme.colorScheme.outline,
        iconTintColor = MaterialTheme.colorScheme.surfaceVariant,
        iconBorderColor = MaterialTheme.colorScheme.outline,
    )

    @Composable
    fun errorColors() = SearchMapColors(
        buttonContentColor = MaterialTheme.colorScheme.error,
        dividerColor = MaterialTheme.colorScheme.error,
        iconContainerColor = MaterialTheme.colorScheme.error,
        iconTintColor = MaterialTheme.colorScheme.surfaceVariant,
        iconBorderColor = MaterialTheme.colorScheme.error,
    )

    @Composable
    fun successColors() = SearchMapColors(
        buttonContentColor = MaterialTheme.colorScheme.primary,
        dividerColor = MaterialTheme.colorScheme.primary,
        iconContainerColor = MaterialTheme.colorScheme.primary,
        iconTintColor = MaterialTheme.colorScheme.surfaceVariant,
        iconBorderColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Preview
@Composable
private fun PreviewSearchMap() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            SearchMap(uiState = CreatePlaceViewModel.UiState(), onAction = {})
        }
    }
}