package org.codingforanimals.veganuniverse.create.presentation.place.entity

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import org.codingforanimals.veganuniverse.create.domain.place.PlaceAddressDomainEntity
import org.codingforanimals.veganuniverse.create.domain.place.PlaceFormDomainEntity
import org.codingforanimals.veganuniverse.create.presentation.common.AddressField
import org.codingforanimals.veganuniverse.create.presentation.common.LocationField
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel

internal fun CreatePlaceViewModel.UiState.toPlaceForm(): PlaceFormDomainEntity? {
    if (!areFieldsValid()) return null
    val latLngUID = locationField.getUniqueIdentifier() ?: return null
    val geoHash = locationField.getGeoHash() ?: return null
    val addressComponents = addressField.toDomainEntity() ?: return null
    val type = typeField.value?.name ?: return null
    val tags = selectedTagsField.tags.map { it.name }
    locationField.toString()
    return PlaceFormDomainEntity(
        latLngUID = latLngUID,
        geoHash = geoHash,
        addressComponents = addressComponents,
        name = nameField.value,
        openingHours = openingHoursField.value,
        type = type,
        tags = tags,
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

private fun LocationField.getUniqueIdentifier(): String? {
    return latLng?.let { "${it.latitude}:${it.longitude}" }
}

private fun LocationField.getGeoHash(): String? {
    return latLng?.let {
        GeoFireUtils.getGeoHashForLocation(GeoLocation(it.latitude, it.longitude), 22)
    }
}
