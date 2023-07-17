package org.codingforanimals.veganuniverse.create.domain.di

import org.codingforanimals.veganuniverse.create.domain.ContentCreator
import org.codingforanimals.veganuniverse.create.domain.ContentCreatorImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val createFeatureDomainModule = module {
    factoryOf(::ContentCreatorImpl) bind ContentCreator::class
}