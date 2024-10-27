package org.codingforanimals.veganuniverse.place.presentation.create.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.components.VUTextField
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.commons.ui.create.CreateContentHero
import org.codingforanimals.veganuniverse.commons.ui.create.HeroAnchorDefaults
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.create.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.place.presentation.create.CreatePlaceViewModel.UiState
import org.codingforanimals.veganuniverse.place.presentation.create.model.AddressField
import org.codingforanimals.veganuniverse.place.presentation.create.model.LocationField
import org.codingforanimals.veganuniverse.place.presentation.create.model.TypeField

@Composable
internal fun SearchMap(
    uiState: UiState,
    onAction: (Action) -> Unit,
) = with(uiState) {
    val heroAnchorColors = when {
        locationField.isValid -> HeroAnchorDefaults.primaryColors()
        isValidating -> HeroAnchorDefaults.errorColors()
        else -> HeroAnchorDefaults.secondaryColors()
    }

    CreateContentHero(
        heroAnchorIcon = VUIcons.LocationFilled,
        heroAnchorColors = heroAnchorColors,
        content = {
            Map(
                cameraPositionState = cameraPositionState,
                location = locationField,
                typeField = typeField,
                onAction = onAction,
            )
            Foreground(
                uiState = uiState,
                onAction = onAction,
            )
        }
    )
}

@Composable
private fun Map(
    cameraPositionState: CameraPositionState,
    location: LocationField,
    typeField: TypeField,
    onAction: (Action) -> Unit,
) {
    val blur = animateDpAsState(
        targetValue = if (!location.isValid) 5.dp else 0.dp,
        label = "create_place_map_blur",
    )
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .blur(blur.value)
            .clickable { onAction(Action.OnSearchMapClick) },
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
    uiState: UiState,
    onAction: (Action) -> Unit,
) = with(uiState) {
    Crossfade(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onAction(Action.OnSearchMapClick) },
        targetState = addressField != null,
        label = "create_place_foreground_animation",
    ) { addressSelected ->
        if (addressSelected && addressField != null) {
            PlaceLocationForeground(addressField, isValidating, onAction)
        } else {
            SearchPlaceForeground(uiState, onAction)
        }
    }
}

@Composable
private fun SearchPlaceForeground(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    val buttonBorderColor = when {
        uiState.isValidating && !uiState.locationField.isValid -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        OutlinedButton(
            onClick = { onAction(Action.OnSearchMapClick) },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = BorderStroke(1.dp, buttonBorderColor),
        ) {
            VUIcon(icon = VUIcons.Search, contentDescription = "", tint = buttonBorderColor)
            Text(
                modifier = Modifier.padding(start = Spacing_03),
                text = stringResource(R.string.place_search_map_prompt_button_label),
                color = buttonBorderColor,
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
            value = addressField.streetAddress,
            onValueChange = { onAction(Action.OnFormChange(address = it)) },
            isError = isValidating && !addressField.isValid,
            leadingIcon = VUIcons.Location,
            maxLines = 2,
        )
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

@Preview
@Composable
private fun PreviewSearchMap() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            SearchMap(uiState = UiState(), onAction = {})
        }
    }
}