@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.place.presentation.details.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_07
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.model.OpeningHoursUI
import org.codingforanimals.veganuniverse.place.presentation.model.PlaceMarker
import org.codingforanimals.veganuniverse.place.presentation.model.displayPeriod
import org.codingforanimals.veganuniverse.place.presentation.model.label
import org.codingforanimals.veganuniverse.place.presentation.model.toUI
import org.codingforanimals.veganuniverse.place.presentation.utils.mapStyleJson
import org.codingforanimals.veganuniverse.place.shared.model.PlaceTag

@Composable
internal fun Actions(
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onEditClick: () -> Unit,
    onReportClick: () -> Unit,
) {
    Row {
        IconButton(onClick = onBookmarkClick) {
            val icon = remember(isBookmarked) {
                if (isBookmarked) VUIcons.BookmarkFilled.id
                else VUIcons.Bookmark.id
            }
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(icon),
                contentDescription = null,
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(VUIcons.Edit.id),
                contentDescription = null,
            )
        }
        IconButton(onClick = onReportClick) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(VUIcons.Report.id),
                contentDescription = null,
            )
        }
    }
}

@Composable
internal fun Address(
    modifier: Modifier = Modifier,
    fullStreetAddress: String,
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = VUIcons.Location.id),
                contentDescription = null,
            )
            Text(
                text = "Dirección",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            modifier = Modifier.padding(start = Spacing_07),
            text = fullStreetAddress,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
internal fun OpeningHours(
    modifier: Modifier = Modifier,
    openingHours: List<OpeningHoursUI>,
) {
    Column(modifier) {
        var openingHoursExpanded by rememberSaveable { mutableStateOf(false) }
        Row(
            modifier = Modifier.clickable { openingHoursExpanded = !openingHoursExpanded },
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            VUIcon(icon = VUIcons.Clock)
            Text(
                modifier = Modifier
                    .padding(vertical = Spacing_03)
                    .weight(1f),
                text = stringResource(R.string.opening_hours),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            val rotationState by animateFloatAsState(
                targetValue = if (openingHoursExpanded) 180f else 0f,
                label = "chevron-rotation-value"
            )
            VUIcon(
                modifier = Modifier.rotate(rotationState),
                icon = VUIcons.ChevronDown,
            )
        }
        AnimatedVisibility(
            modifier = Modifier.padding(
                top = Spacing_03,
                start = Spacing_07,
            ),
            visible = openingHoursExpanded
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing_04)) {
                openingHours.forEach { openingHours ->
                    key(openingHours.dayOfWeek) {
                        Row {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(openingHours.dayOfWeek.label),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            openingHours.mainPeriod?.let { mainPeriod ->
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = mainPeriod.displayPeriod,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    openingHours.secondaryPeriod?.let { secondaryPeriod ->
                                        Text(
                                            modifier = Modifier.padding(top = Spacing_02),
                                            text = secondaryPeriod.displayPeriod,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            } ?: Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.closed),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun FlowRowTags(
    modifier: Modifier = Modifier,
    tags: List<PlaceTag>,
) {
    val isListOddNumbered = remember { tags.size.rem(2) == 1 }
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        maxItemsInEachRow = 2,
    ) {
        tags.forEachIndexed { index, tag ->
            key(index) {
                val tagUI = tag.toUI()
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = Spacing_04),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_04),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    VUIcon(
                        icon = tagUI.icon,
                        contentDescription = stringResource(tagUI.label),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(tagUI.label),
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }
        if (isListOddNumbered) Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
internal fun StaticMap(
    modifier: Modifier = Modifier,
    marker: PlaceMarker,
    cameraPositionState: CameraPositionState,
) {
    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
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
                ?.let { BitmapDescriptorFactory.fromBitmap(it) }
        )
    }
}