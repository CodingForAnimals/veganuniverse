package org.codingforanimals.veganuniverse.validator.navigation

import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.validator.R

internal enum class ValidatorTopLevelDestination(
    val destination: ValidatorDestination,
    val unselectedIcon: Icon,
    val selectedIcon: Icon,
    val label: Int,
) {
    PRODUCTS(
        destination = ValidatorDestination.ValidateProducts,
        unselectedIcon = VUIcons.VeganLogo,
        selectedIcon = VUIcons.VeganLogo,
        label = R.string.nav_label_is_vegan
    ),
    PLACES(
        destination = ValidatorDestination.ValidatePlaces,
        unselectedIcon = VUIcons.Location,
        selectedIcon = VUIcons.LocationFilled,
        label = R.string.nav_label_places
    ),
}
