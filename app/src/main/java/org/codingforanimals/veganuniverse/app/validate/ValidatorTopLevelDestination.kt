package org.codingforanimals.veganuniverse.app.validate

import org.codingforanimals.veganuniverse.app.R
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

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
        label = R.string.validate_bottom_nav_label_products
    ),
    PLACES(
        destination = ValidatorDestination.ValidatePlaces,
        unselectedIcon = VUIcons.Location,
        selectedIcon = VUIcons.LocationFilled,
        label = R.string.validate_bottom_nav_label_places
    ),
}
