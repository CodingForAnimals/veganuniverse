package org.codingforanimals.veganuniverse.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.EntityMapper
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.services.firebase.entity.AddressComponents
import org.codingforanimals.veganuniverse.services.firebase.entity.OpeningHours
import org.codingforanimals.veganuniverse.services.firebase.entity.Place
import org.codingforanimals.veganuniverse.places.entity.AddressComponents as AddressComponentsDomainEntity
import org.codingforanimals.veganuniverse.places.entity.OpeningHours as OpeningHoursDomainEntity
import org.codingforanimals.veganuniverse.places.entity.Place as PlaceDomainEntity

internal class PlaceToDomainEntityMapper(
    private val addressComponentsMapper: EntityMapper<AddressComponents, AddressComponentsDomainEntity>,
    private val openingHoursMapper: EntityMapper<OpeningHours, OpeningHoursDomainEntity>,
) : OneWayEntityMapper<Place, PlaceDomainEntity> {

    override fun map(obj: Place): PlaceDomainEntity {
        return with(obj) {
            PlaceDomainEntity(
                geoHash = geoHash,
                name = name,
                addressComponents = addressComponentsMapper.mapHigher(addressComponents),
                imageRef = imageRef,
                type = type,
                description = description,
                rating = rating,
                tags = tags,
                latitude = latitude,
                longitude = longitude,
                timestamp = timestamp?.seconds ?: 0L,
                openingHours = openingHours.map { openingHoursMapper.mapHigher(it) },
            )
        }
    }
}