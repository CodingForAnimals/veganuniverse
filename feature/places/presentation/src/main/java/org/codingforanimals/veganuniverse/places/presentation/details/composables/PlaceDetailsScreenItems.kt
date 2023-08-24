@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.places.presentation.details.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import org.codingforanimals.veganuniverse.core.common.R.string.closed
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.places.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.entity.utils.fullStreetAddress
import org.codingforanimals.veganuniverse.places.presentation.details.model.OpeningHours
import org.codingforanimals.veganuniverse.places.presentation.details.model.PlaceMarker
import org.codingforanimals.veganuniverse.places.presentation.utils.mapStyleJson

@Composable
internal fun AddressAndOpeningHours(
    addressComponents: AddressComponents,
    openingHours: List<OpeningHours>,
) {
    Column(
        modifier = Modifier.padding(horizontal = Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_06),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            VUIcon(icon = VUIcons.Location, contentDescription = "")
            Text(text = addressComponents.fullStreetAddress)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing_06),
        ) {
            VUIcon(icon = VUIcons.Clock, contentDescription = "")
            Column(verticalArrangement = Arrangement.spacedBy(Spacing_03)) {
                openingHours.forEach { openingHours ->
                    key(openingHours.dayOfWeek) {
                        Row {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(openingHours.dayOfWeek.day),
                            )
                            openingHours.mainPeriod?.let { mainPeriod ->
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = mainPeriod.displayPeriod)
                                    openingHours.secondaryPeriod?.let { secondaryPeriod ->
                                        Text(text = secondaryPeriod.displayPeriod)
                                    }
                                }
                            } ?: Text(modifier = Modifier.weight(1f), text = stringResource(closed))
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun FlowRowTags(
    tags: List<PlaceTag>,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_06),
        maxItemsInEachRow = 2,
    ) {
        tags.forEachIndexed { index, tag ->
            val isSingleTagRow = (index == (tags.size - 1)) && (index.rem(2) == 0)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = Spacing_04),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                VUIcon(
                    icon = tag.icon,
                    contentDescription = stringResource(tag.label),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(text = stringResource(tag.label))
            }
            if (isSingleTagRow) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
internal fun StaticMap(
    marker: PlaceMarker,
    cameraPositionState: CameraPositionState,
) {
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_06)
            .aspectRatio(2f)
            .clip(ShapeDefaults.Medium),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions(mapStyleJson),
        ),
        uiSettings = MapUiSettings(
            compassEnabled = false,
            indoorLevelPickerEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false,
            rotationGesturesEnabled = false,
            scrollGesturesEnabled = false,
            scrollGesturesEnabledDuringRotateOrZoom = false,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = false,
        )
    ) {
        Marker(
            state = rememberMarkerState(position = cameraPositionState.position.target),
            icon = marker.getDisplayMarker(isSelected = true)
        )
    }
}