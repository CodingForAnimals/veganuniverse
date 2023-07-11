package org.codingforanimals.veganuniverse.create.presentation.place.entity

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import org.codingforanimals.veganuniverse.create.domain.model.PlaceAddressDomainEntity
import org.codingforanimals.veganuniverse.create.domain.model.PlaceFormDomainEntity
import org.codingforanimals.veganuniverse.create.presentation.model.AddressField
import org.codingforanimals.veganuniverse.create.presentation.model.LocationField
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel

internal fun CreatePlaceViewModel.UiState.toPlaceForm(): PlaceFormDomainEntity? {
    if (!areFieldsValid()) return null
    val latLng = locationField.latLng ?: return null
    val geoHash = locationField.getGeoHash() ?: return null
    val addressComponents = addressField.toDomainEntity() ?: return null
    val type = typeField.value?.name ?: return null
    val tags = selectedTagsField.tags.map { it.name }
    locationField.toString()
    return PlaceFormDomainEntity(
        geoHash = geoHash,
        addressComponents = addressComponents,
        name = nameField.value,
        description = descriptionField.value,
        openingHours = openingHoursField.value,
        type = type,
        tags = tags,
        latitude = latLng.latitude,
        longitude = latLng.longitude,
    )
}

private fun CreatePlaceViewModel.UiState.areFieldsValid(): Boolean {
    val fields = listOf(
        locationField,
        addressField,
        nameField,
        openingHoursField,
        pictureField,
        typeField,
        descriptionField,
        selectedTagsField,
    )
    for (field in fields) {
        if (!field.isValid) return false
    }
    return true
}

private fun AddressField.toDomainEntity(): PlaceAddressDomainEntity? {
    return PlaceAddressDomainEntity(
        streetAddress = streetAddress ?: return null,
        locality = locality,
        province = province,
        country = country
    )
}

private fun LocationField.getGeoHash(): String? {
    return latLng?.let {
        GeoFireUtils.getGeoHashForLocation(GeoLocation(it.latitude, it.longitude), 22)
    }
}
