package org.codingforanimals.veganuniverse.validator.di

import org.codingforanimals.veganuniverse.validator.place.di.placeModule
import org.codingforanimals.veganuniverse.validator.product.di.productModule
import org.koin.dsl.module

val validatorModule = module {
    includes(
        productModule,
        placeModule,
    )
}