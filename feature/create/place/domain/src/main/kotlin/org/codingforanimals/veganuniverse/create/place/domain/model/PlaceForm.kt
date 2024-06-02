package org.codingforanimals.veganuniverse.create.place.domain.model

import android.os.Parcelable
import org.codingforanimals.veganuniverse.place.model.AddressComponents
import org.codingforanimals.veganuniverse.place.model.OpeningHours
import org.codingforanimals.veganuniverse.place.model.PlaceTag
import org.codingforanimals.veganuniverse.place.model.PlaceType

data class PlaceForm(
    val name: String,
    val imageModel: Parcelable,
    val addressComponents: AddressComponents,
    val openingHours: List<OpeningHours>,
    val description: String,
    val type: PlaceType,
    val latitude: Double,
    val longitude: Double,
    val tags: List<PlaceTag>,
)