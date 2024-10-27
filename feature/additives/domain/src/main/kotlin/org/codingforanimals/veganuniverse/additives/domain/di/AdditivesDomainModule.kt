package org.codingforanimals.veganuniverse.additives.domain.di

import org.codingforanimals.veganuniverse.additives.data.di.additivesDataModule
import org.codingforanimals.veganuniverse.additives.domain.repository.AdditiveRepository
import org.codingforanimals.veganuniverse.additives.domain.repository.AdditiveRepositoryImpl
import org.codingforanimals.veganuniverse.additives.domain.repository.AdditivesConfigRepository
import org.codingforanimals.veganuniverse.additives.domain.repository.AdditivesConfigRepositoryImpl
import org.codingforanimals.veganuniverse.additives.domain.usecase.CheckForAdditivesUpdate
import org.codingforanimals.veganuniverse.additives.domain.usecase.AdditivesUseCases
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val additivesDomainModule = module {
    includes(
        additivesDataModule,
    )

    factoryOf(::AdditivesConfigRepositoryImpl) bind AdditivesConfigRepository::class
    factoryOf(::AdditiveRepositoryImpl) bind AdditiveRepository::class
    factoryOf(::AdditivesUseCases)
    factoryOf(::CheckForAdditivesUpdate)
}
