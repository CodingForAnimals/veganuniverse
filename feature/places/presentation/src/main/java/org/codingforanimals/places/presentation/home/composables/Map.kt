//package org.codingforanimals.places.presentation.home.composables
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedCard
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.google.maps.android.compose.CameraPositionState
//import com.google.maps.android.compose.GoogleMap
//import com.google.maps.android.compose.MapProperties
//import com.google.maps.android.compose.MapUiSettings
//import com.google.maps.android.compose.MarkerInfoWindow
//import org.codingforanimals.places.presentation.home.OldPlacesHomeViewModel
//import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
//import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
//import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
//
//@Composable
//internal fun Map(
//    modifier: Modifier = Modifier,
//    uiState: OldPlacesHomeViewModel.UiState,
//    cameraPositionState: CameraPositionState,
//    onMarkerClick: (String) -> Unit,
//    onMapClick: () -> Unit,
//) {
//    Box(
//        modifier = Modifier.wrapContentSize()
//    ) {
//        GoogleMap(
//            modifier = modifier,
//            onMapClick = { onMapClick() },
//            onPOIClick = { onMapClick() },
//            cameraPositionState = cameraPositionState,
//            uiSettings = MapUiSettings(
//                mapToolbarEnabled = false,
//                zoomControlsEnabled = false
//            ),
//            properties = MapProperties(isMyLocationEnabled = uiState.locationGranted),
//        ) {
//            uiState.markers.forEach { marker ->
//                val isSelected = uiState.selectedPlaceCard?.id == marker.id
//                val placeMarker = when (marker.type) {
//                    PlaceType.STORE -> Markers.storeMarker
//                    PlaceType.RESTAURANT -> Markers.restaurantMarker
//                    PlaceType.CAFE -> Markers.cafeMarker
//                    else -> Markers.storeMarker
//                }
//
//                MarkerInfoWindow(
//                    state = marker.state,
//                    icon = placeMarker.getDisplayMarker(isSelected),
//                    onClick = {
//                        it.showInfoWindow()
//                        onMarkerClick(marker.id)
//                        true
//                    },
//                    content = {
//                        OutlinedCard(
//                            colors = CardDefaults.cardColors(
//                                containerColor = MaterialTheme.colorScheme.onPrimary,
//                                contentColor = MaterialTheme.colorScheme.primary,
//                            ),
//                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
//                        ) {
//                            Text(
//                                modifier = Modifier.padding(
//                                    horizontal = Spacing_04,
//                                    vertical = Spacing_02
//                                ),
//                                text = marker.name,
//                                fontWeight = FontWeight.SemiBold,
//                            )
//                        }
//                    }
//                )
//            }
//        }
//        AnimatedVisibility(
//            visible = uiState.areMarkersLoading,
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//            Box(modifier = Modifier
//                .fillMaxSize()
//                .clickable {}) {
//                CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.Center)
//                )
//            }
//        }
//    }
//}