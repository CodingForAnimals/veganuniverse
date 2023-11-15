package org.codingforanimals.veganuniverse.create.place.presentation.usecase

import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.EnterDescription
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.EnterName
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.EnterOpeningHours
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.Map
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.SelectIcon
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.SelectImage
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.SelectTags
import org.codingforanimals.veganuniverse.create.place.presentation.entity.CreatePlaceFormItem.SubmitButton

class GetCreatePlaceScreenContent {

    operator fun invoke(): List<CreatePlaceFormItem> {
        return listOf(
            Map,
            EnterName,
            EnterOpeningHours,
            SelectImage,
            SelectIcon,
            EnterDescription,
            SelectTags,
            SubmitButton,
        )
    }
}
