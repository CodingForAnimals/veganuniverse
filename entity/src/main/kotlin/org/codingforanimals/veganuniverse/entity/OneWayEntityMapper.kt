package org.codingforanimals.veganuniverse.entity

interface OneWayEntityMapper<T, V> {
    fun map(obj: T): V
}