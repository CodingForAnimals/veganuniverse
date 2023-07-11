package org.codingforanimals.veganuniverse.create.domain.model

fun PlaceFormDomainEntity.toFirebaseEntity() = PlaceFormFirebaseEntity(
    name = name,
    openingHours = openingHours,
    type = type,
    address = "address",
    city = "city",
    tags = tags,
    longitude = longitude,
    latitude = latitude,
    geoHash = geoHash,
)