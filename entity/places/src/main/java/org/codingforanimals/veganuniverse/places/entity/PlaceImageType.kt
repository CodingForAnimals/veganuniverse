package org.codingforanimals.veganuniverse.places.entity

sealed class PlaceImageType {
    data object Thumbnail : PlaceImageType()
    data object Picture : PlaceImageType()
}
