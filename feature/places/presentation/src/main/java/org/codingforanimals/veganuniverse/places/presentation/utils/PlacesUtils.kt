package org.codingforanimals.veganuniverse.places.presentation.utils

import android.location.Location
import com.google.maps.android.compose.CameraPositionState
import kotlin.math.pow
import kotlin.math.sqrt

// Not ideal. This should sit in the resources folder.
internal const val mapStyleJson = "[\n" +
    "  {\n" +
    "    \"featureType\": \"administrative\",\n" +
    "    \"elementType\": \"geometry\",\n" +
    "    \"stylers\": [\n" +
    "      {\n" +
    "        \"visibility\": \"on\"\n" +
    "      }\n" +
    "    ]\n" +
    "  },\n" +
    "  {\n" +
    "    \"featureType\": \"poi\",\n" +
    "    \"stylers\": [\n" +
    "      {\n" +
    "        \"visibility\": \"off\"\n" +
    "      }\n" +
    "    ]\n" +
    "  },\n" +
    "  {\n" +
    "    \"featureType\": \"road\",\n" +
    "    \"elementType\": \"labels.icon\",\n" +
    "    \"stylers\": [\n" +
    "      {\n" +
    "        \"visibility\": \"off\"\n" +
    "      }\n" +
    "    ]\n" +
    "  },\n" +
    "  {\n" +
    "    \"featureType\": \"transit\",\n" +
    "    \"stylers\": [\n" +
    "      {\n" +
    "        \"visibility\": \"off\"\n" +
    "      }\n" +
    "    ]\n" +
    "  }\n" +
    "]"

internal fun CameraPositionState.visibleRadiusInKm(): Double {
    this.projection?.visibleRegion?.let { visibleRegion ->
        val distanceWidth = FloatArray(1)
        val distanceHeight = FloatArray(1)
        val farRight = visibleRegion.farRight
        val farLeft = visibleRegion.farLeft
        val nearRight = visibleRegion.nearRight
        val nearLeft = visibleRegion.nearLeft

        Location.distanceBetween(
            (farLeft.latitude + nearLeft.latitude) / 2,
            farLeft.longitude,
            (farRight.latitude + nearRight.latitude) / 2,
            farRight.longitude,
            distanceWidth
        )
        Location.distanceBetween(
            farRight.latitude,
            (farRight.longitude + farLeft.longitude) / 2,
            nearRight.latitude,
            (nearRight.longitude + nearLeft.longitude) / 2,
            distanceHeight
        )
        return (sqrt(
            distanceWidth[0].toDouble().pow(2.0) + distanceHeight[0].toDouble().pow(2.0)
        ) / 2) / 1000
    }
    return 0.0
}