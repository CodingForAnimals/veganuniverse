@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.presentation.place.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import org.codingforanimals.veganuniverse.core.ui.R.drawable.ic_location_filled
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTextFieldDefaults
import org.codingforanimals.veganuniverse.core.ui.components.rememberBitmap
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel


@Composable
internal fun EnterLocation(
    address: String,
    addressError: Boolean,
    location: LatLng?,
    locationError: Boolean,
    onAddressChange: (String) -> Unit,
    onAddressSearch: () -> Unit,
    addressCandidates: List<CreatePlaceViewModel.PlaceAddress>,
    onDialogDismissed: () -> Unit,
    onCandidateSelected: (Int) -> Unit,
) {
    val mapPlaceholderColor =
        if (locationError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
    val focusManager = LocalFocusManager.current
    val cameraPositionState = rememberCameraPositionState()
    val markerState = rememberMarkerState()
    LaunchedEffect(location) {
        location?.let {
            markerState.position = location
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 16f)
            cameraPositionState.animate(cameraUpdate, 2500)
        }
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_06, vertical = Spacing_02)
            .animateContentSize(),
        value = address,
        isError = addressError,
        onValueChange = onAddressChange,
        placeholder = { Text(text = "Dirección") },
        supportingText = { Text(text = "Ingresar dirección y ciudad") },
        leadingIcon = { VUIcon(icon = VUIcons.Location, contentDescription = "") },
        trailingIcon = {
            VUIcon(
                icon = VUIcons.Search,
                contentDescription = "",
                onIconClick = {
                    focusManager.clearFocus()
                    onAddressSearch()
                },
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onAddressSearch()
            },
        ),
        colors = VUTextFieldDefaults.colors(),
    )
    Box(
        modifier = Modifier
            .padding(Spacing_05)
            .fillMaxWidth()
            .aspectRatio(2f)
            .clip(ShapeDefaults.Medium),
    ) {
        Crossfade(targetState = location != null) { placeLocationAvailable ->
            if (placeLocationAvailable) {
                GoogleMap(
                    cameraPositionState = cameraPositionState,
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
                        zoomGesturesEnabled = false
                    ),
                ) {
                    val bitmap = rememberBitmap(
                        resId = ic_location_filled,
//                        size = DpSize(30.dp, 30.dp),
//                        tint = MaterialTheme.colorScheme.primary,
                    )
                    val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
                    Marker(
                        icon = bitmapDescriptor,
                        state = markerState,
                    )
                }
            } else {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.img_map),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xADA1A1AA))
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    VUIcon(
                        modifier = Modifier.padding(bottom = Spacing_04),
                        icon = VUIcons.LocationFilled,
                        contentDescription = "",
                        tint = mapPlaceholderColor,
                    )
                    Text(text = "Buscá la dirección del lugar", color = mapPlaceholderColor)
                }
            }
        }
    }

    if (addressCandidates.isNotEmpty()) {
        Dialog(onDismissRequest = onDialogDismissed) {
            Card {
                Column(
                    modifier = Modifier.padding(Spacing_05),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    addressCandidates.forEachIndexed { index, candidate ->
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onCandidateSelected(index) }
                        ) {
                            Text(candidate.address)
                        }
                        Spacer(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .width(200.dp)
                                .height(2.dp)
                                .padding(vertical = Spacing_06)
                        )
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onDialogDismissed
                        ) {
                            Text(candidate.address)
                        }
                        Spacer(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .width(200.dp)
                                .height(2.dp)
                                .padding(vertical = Spacing_06)
                        )
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onDialogDismissed
                        ) {
                            Text(candidate.address)
                        }
                    }
                }
            }
        }
    }
}