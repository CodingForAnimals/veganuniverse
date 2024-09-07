package org.codingforanimals.veganuniverse.validator.navigation

import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

internal enum class ValidatorTopLevelDestination(
    val destination: ValidatorDestination,
    val unselectedIcon: Icon,
    val selectedIcon: Icon,
) {
    PRODUCTS(
        destination = ValidatorDestination.ValidateProducts,
        unselectedIcon = VUIcons.VeganLogo,
        selectedIcon = VUIcons.VeganLogo,
    ),
    PLACES(
        destination = ValidatorDestination.ValidatePlaces,
        unselectedIcon = VUIcons.Location,
        selectedIcon = VUIcons.LocationFilled,
    ),
}
