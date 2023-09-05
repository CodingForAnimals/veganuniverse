package org.codingforanimals.veganuniverse.services.firebase.entity.mapper

import android.content.Context
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.PlaceImageType
import org.codingforanimals.veganuniverse.services.firebase.StoragePath
import org.codingforanimals.veganuniverse.services.firebase.entity.PlaceCard
import org.codingforanimals.veganuniverse.services.firebase.model.GetPlaceResult
import org.codingforanimals.veganuniverse.services.firebase.places.R
import org.codingforanimals.veganuniverse.services.firebase.utils.extension
import org.codingforanimals.veganuniverse.places.entity.PlaceCard as PlaceCardDomainEntity

internal class GetPlaceResultToPlaceCardMapper(
    context: Context,
) :
    OneWayEntityMapper<GetPlaceResult, PlaceCardDomainEntity> {

    private val storageBucket = context.getString(R.string.firebase_storage_bucket)

    override fun map(obj: GetPlaceResult): PlaceCardDomainEntity {
        val placeCard = obj.dataSnapshot.getValue(PlaceCard::class.java)!!
        val geoHash = obj.dataSnapshot.key!!
        val imageRef = StoragePath.getPlaceImageRef(
            storageBucket,
            geoHash,
            PlaceImageType.Thumbnail.extension
        )
        return with(placeCard) {
            PlaceCardDomainEntity(
                geoHash = geoHash,
                name = name,
                rating = rating,
                streetAddress = streetAddress,
                administrativeArea = administrativeArea,
                type = type,
                imageRef = imageRef,
                tags = tags,
                timestamp = timestamp,
                latitude = obj.geoLocation.latitude,
                longitude = obj.geoLocation.longitude,
            )
        }
    }
}