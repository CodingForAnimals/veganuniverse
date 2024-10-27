package org.codingforanimals.veganuniverse.place.model

import android.os.Parcelable
import org.codingforanimals.veganuniverse.place.shared.model.AddressComponents
import org.codingforanimals.veganuniverse.place.shared.model.OpeningHours
import org.codingforanimals.veganuniverse.place.shared.model.PlaceTag
import org.codingforanimals.veganuniverse.place.shared.model.PlaceType

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