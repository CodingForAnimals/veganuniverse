package org.codingforanimals.veganuniverse.product.services.firebase.di

import org.codingforanimals.veganuniverse.product.services.firebase.ProductFirebaseService
import org.codingforanimals.veganuniverse.product.services.firebase.ProductService
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val productFirebaseModule = module {
    includes(firebaseServiceModule)
    factoryOf(::ProductFirebaseService) bind ProductService::class
}
