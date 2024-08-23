package org.codingforanimals.veganuniverse.create.product.domain.di

import org.codingforanimals.veganuniverse.create.product.domain.ProductCreator
import org.codingforanimals.veganuniverse.create.product.domain.ProductCreatorImpl
import org.codingforanimals.veganuniverse.product.services.firebase.di.productFirebaseModule
import org.codingforanimals.veganuniverse.profile.services.firebase.di.profileFirebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val createProductDomainModule = module {
    includes(
        productFirebaseModule,
        profileFirebaseServiceModule
    )
    factoryOf(::ProductCreatorImpl) bind ProductCreator::class
}
