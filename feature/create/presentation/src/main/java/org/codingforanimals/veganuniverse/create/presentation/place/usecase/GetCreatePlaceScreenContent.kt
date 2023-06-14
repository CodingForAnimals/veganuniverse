package org.codingforanimals.veganuniverse.create.presentation.place.usecase

import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.EnterDescription
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.EnterName
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.EnterOpeningHours
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.Map
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.SelectIcon
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.SelectImage
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.SelectTags
import org.codingforanimals.veganuniverse.create.presentation.place.entity.CreatePlaceFormItem.SubmitButton

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
