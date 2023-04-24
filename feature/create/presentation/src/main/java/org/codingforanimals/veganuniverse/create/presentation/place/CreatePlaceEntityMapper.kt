package org.codingforanimals.veganuniverse.create.presentation.place

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import org.codingforanimals.veganuniverse.create.domain.place.PlaceFormDomainEntity

internal fun PlaceFormViewEntity.toDomainEntity() =
    PlaceFormDomainEntity(
        id = getId(),
        type = type.name,
        name = name,
        openingHours = openingHours,
        address = address,
        city = "city..",
        tags = tags.map { it.name },
        geoHash = getGeoHash(),
    )

private fun PlaceFormViewEntity.getId(): String = "$latitude:$longitude"

private fun PlaceFormViewEntity.getGeoHash(): String =
    GeoFireUtils.getGeoHashForLocation(GeoLocation(latitude, longitude), 22)