package org.codingforanimals.veganuniverse.core.location.model

sealed class LocationResponse {
    object LocationLoading : LocationResponse()
    object LocationNotRequested : LocationResponse()
    object PermissionsNotGranted : LocationResponse()
    object LocationServiceDisabled : LocationResponse()
    object UnknownError : LocationResponse()
    data class LocationGranted(val latitude: Double, val longitude: Double) : LocationResponse()
}