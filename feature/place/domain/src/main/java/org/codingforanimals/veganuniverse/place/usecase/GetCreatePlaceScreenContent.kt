package org.codingforanimals.veganuniverse.place.usecase

import org.codingforanimals.veganuniverse.place.model.CreatePlaceFormItem
import org.codingforanimals.veganuniverse.place.model.CreatePlaceFormItem.*

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
