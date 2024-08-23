package org.codingforanimals.veganuniverse.services.location.model

sealed class LocationResponse {
    data object LocationLoading : LocationResponse()
    data object LocationNotRequested : LocationResponse()
    data object PermissionsNotGranted : LocationResponse()
    data object LocationServiceDisabled : LocationResponse()
    data object UnknownError : LocationResponse()
    data class LocationGranted(val latitude: Double, val longitude: Double) : LocationResponse()
}