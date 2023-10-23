package org.codingforanimals.veganuniverse.places.services.entity.mapper

import android.content.Context
import org.codingforanimals.veganuniverse.entity.EntityMapper
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.PlaceImageType
import org.codingforanimals.veganuniverse.places.services.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.services.entity.OpeningHours
import org.codingforanimals.veganuniverse.places.services.entity.Place
import org.codingforanimals.veganuniverse.places.services.firebase.R
import org.codingforanimals.veganuniverse.places.services.utils.extension
import org.codingforanimals.veganuniverse.services.firebase.StoragePath
import org.codingforanimals.veganuniverse.places.entity.AddressComponents as AddressComponentsDomainEntity
import org.codingforanimals.veganuniverse.places.entity.OpeningHours as OpeningHoursDomainEntity
import org.codingforanimals.veganuniverse.places.entity.Place as PlaceDomainEntity

internal class PlaceToDomainEntityMapper(
    context: Context,
    private val addressComponentsMapper: EntityMapper<AddressComponents, AddressComponentsDomainEntity>,
    private val openingHoursMapper: EntityMapper<OpeningHours, OpeningHoursDomainEntity>,
) : OneWayEntityMapper<Place, PlaceDomainEntity> {

    private val storageBucket = context.getString(R.string.firebase_storage_bucket)

    override fun map(obj: Place): PlaceDomainEntity {
        return with(obj) {
            val imageRef = StoragePath.getPlaceImageRef(
                storageBucket,
                geoHash,
                PlaceImageType.Picture.extension
            )
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