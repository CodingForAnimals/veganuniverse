package org.codingforanimals.veganuniverse.create.presentation.place.entity

sealed class CreatePlaceFormItem {
    object Map : CreatePlaceFormItem()
    object EnterName : CreatePlaceFormItem()
    object EnterOpeningHours : CreatePlaceFormItem()
    object SelectImage : CreatePlaceFormItem()
    object SelectIcon : CreatePlaceFormItem()
    object EnterDescription : CreatePlaceFormItem()
    object SelectTags : CreatePlaceFormItem()
    object SubmitButton : CreatePlaceFormItem()
}