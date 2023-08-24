package org.codingforanimals.veganuniverse.entity

interface EntityMapper<T, V> {
    fun mapHigher(obj: T): V
    fun mapLower(obj: V): T
}