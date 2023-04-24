package org.codingforanimals.places.presentation.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

private const val TAG = "PlacesMapUtils"

// Not ideal. This should sit in the resources folder.
internal const val mapStyleJson = "[\n" +
    "  {\n" +
    "    \"featureType\": \"administrative\",\n" +
    "    \"elementType\": \"geometry\",\n" +
    "    \"stylers\": [\n" +
    "      {\n" +
    "        \"visibility\": \"off\"\n" +
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


internal fun LatLng.distanceTo(latLng: LatLng): Double =
    SphericalUtil.computeDistanceBetween(this, latLng)