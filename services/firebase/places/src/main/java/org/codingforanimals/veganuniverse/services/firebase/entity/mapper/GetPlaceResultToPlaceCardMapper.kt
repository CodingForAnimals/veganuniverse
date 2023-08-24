package org.codingforanimals.veganuniverse.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.services.firebase.entity.PlaceCard
import org.codingforanimals.veganuniverse.services.firebase.model.GetPlaceResult
import org.codingforanimals.veganuniverse.places.entity.PlaceCard as PlaceCardDomainEntity

internal class GetPlaceResultToPlaceCardMapper :
    OneWayEntityMapper<GetPlaceResult, PlaceCardDomainEntity> {
    override fun map(obj: GetPlaceResult): PlaceCardDomainEntity {
        val placeCard = obj.dataSnapshot.getValue(PlaceCard::class.java)!!

        return with(placeCard) {
            PlaceCardDomainEntity(
                geoHash = obj.dataSnapshot.key!!,
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