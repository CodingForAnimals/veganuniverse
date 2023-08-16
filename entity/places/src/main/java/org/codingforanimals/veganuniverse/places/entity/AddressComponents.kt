package org.codingforanimals.veganuniverse.places.entity

data class AddressComponents(
    val streetAddress: String = "",

    /**
     * Locality in Argentina, i.e. Monte Grande
     */
    val locality: String = "",

    /**
     * Admin area 1
     *
     * Province in Argentina, i.e. Mendoza
     */
    val primaryAdminArea: String = "",

    /**
     * Admin area 2
     *
     * Municipality in Argentina, i.e. Esteban Echeverría
     */
    val secondaryAdminArea: String = "",
    val country: String = "",
)