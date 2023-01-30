package org.codingforanimals.map.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import org.codingforanimals.map.presentation.MapScreen
import org.codingforanimals.map.presentation.argentinaLatLtn
import org.codingforanimals.map.presentation.di.injectMapModules

const val mapNavigationRoute = "map_route"
private const val latitudeArg = "latitude_arg"
private const val longitudeArg = "longitude_arg"
private const val zoomArg = "zoom_arg"

fun NavController.navigateToMap(latitude: Double, longitude: Double, zoom: Float) {
    navigate("$mapNavigationRoute/$latitude/$longitude/$zoom")
}

fun NavGraphBuilder.mapGraph(
    navigateToSite: () -> Unit,
    snackbarHostState: SnackbarHostState,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    injectMapModules()
    composable(
        route = "$mapNavigationRoute/{$latitudeArg}/{$longitudeArg}/{$zoomArg}",
        arguments = listOf(
            navArgument(latitudeArg) { type = NavType.StringType },
            navArgument(longitudeArg) { type = NavType.StringType },
            navArgument(zoomArg) { type = NavType.FloatType },
        )
    ) {
        val latitude = it.arguments?.getString(latitudeArg)?.toDouble()
        val longitude = it.arguments?.getString(longitudeArg)?.toDouble()
        val zoom = it.arguments?.getFloat(zoomArg)
        val latLng = if (latitude == null || longitude == null) {
            argentinaLatLtn
        } else {
            LatLng(latitude, longitude)
        }
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latLng, zoom ?: 4f)
        }
        MapScreen(
            cameraPositionState = cameraPositionState,
            navigateToSite = navigateToSite,
            snackbarHostState = snackbarHostState,
        )
    }
    nestedGraphs()
}