package org.codingforanimals.veganuniverse.place.shared.model

data class AddressComponents(
    val streetAddress: String? = null,

    /**
     * Locality in Argentina, i.e. Monte Grande
     */
    val locality: String? = null,

    /**
     * Admin area 1
     *
     * Province in Argentina, i.e. Mendoza
     */
    val primaryAdminArea: String? = null,

    /**
     * Admin area 2
     *
     * Municipality in Argentina, i.e. Esteban Echeverría
     */
    val secondaryAdminArea: String? = null,
    val country: String? = null,
)