package org.codingforanimals.veganuniverse.create.place.presentation.entity

sealed class CreatePlaceFormItem {
    data object Map : CreatePlaceFormItem()
    data object EnterName : CreatePlaceFormItem()
    data object EnterOpeningHours : CreatePlaceFormItem()
    data object SelectImage : CreatePlaceFormItem()
    data object SelectIcon : CreatePlaceFormItem()
    data object EnterDescription : CreatePlaceFormItem()
    data object SelectTags : CreatePlaceFormItem()
    data object SubmitButton : CreatePlaceFormItem()
}