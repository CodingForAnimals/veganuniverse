package org.codingforanimals.veganuniverse.additives.data.di

import org.codingforanimals.veganuniverse.additives.data.config.di.additivesConfigModule
import org.codingforanimals.veganuniverse.additives.data.source.di.additivesSourceModule
import org.koin.dsl.module

val additivesDataModule = module {
    includes(
        additivesConfigModule,
        additivesSourceModule,
    )
}
