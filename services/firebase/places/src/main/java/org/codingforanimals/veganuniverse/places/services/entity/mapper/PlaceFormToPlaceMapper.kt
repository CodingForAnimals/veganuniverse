package org.codingforanimals.veganuniverse.places.services.entity.mapper

import org.codingforanimals.veganuniverse.entity.EntityMapper
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.places.services.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.services.entity.OpeningHours
import org.codingforanimals.veganuniverse.places.services.entity.Place
import org.codingforanimals.veganuniverse.places.entity.AddressComponents as AddressComponentsDomainEntity
import org.codingforanimals.veganuniverse.places.entity.OpeningHours as OpeningHoursDomainEntity

internal class PlaceFormToPlaceMapper(
    private val openingHoursMapper: EntityMapper<OpeningHours, OpeningHoursDomainEntity>,
    private val addressComponentsMapper: EntityMapper<AddressComponents, AddressComponentsDomainEntity>,
) : OneWayEntityMapper<PlaceForm, Place> {
    override fun map(obj: PlaceForm): Place {
        return with(obj) {
            Place(
                name = name,
                userId = userId,
                rating = 0,
                type = type,
                tags = tags,
                latitude = latitude,
                longitude = longitude,
                openingHours = openingHours.map { openingHoursMapper.mapLower(it) },
                description = description,
                addressComponents = addressComponentsMapper.mapLower(addressComponents),
            )
        }
    }
}