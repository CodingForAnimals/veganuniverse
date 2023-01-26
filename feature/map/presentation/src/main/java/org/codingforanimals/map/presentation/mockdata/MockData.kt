package org.codingforanimals.map.presentation.mockdata

import com.google.android.gms.maps.model.LatLng

val sites = listOf(
    Site("Cheverry", LatLng(-37.327588, -59.135730)),
    Site("Hash Bar", LatLng(-37.333188, -59.130642))
)

data class Site(
    val name: String,
    val latLng: LatLng,
)