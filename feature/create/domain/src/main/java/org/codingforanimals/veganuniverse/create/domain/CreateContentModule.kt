package org.codingforanimals.veganuniverse.create.domain

import org.koin.dsl.module

val createContentModule = module {
    factory<ContentCreator> { ContentCreatorImpl() }
}