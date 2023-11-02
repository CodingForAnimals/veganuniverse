package org.codingforanimals.veganuniverse.places.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.EntityMapper
import org.codingforanimals.veganuniverse.places.services.firebase.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.entity.AddressComponents as AddressComponentsDomainEntity

internal class AddressComponentsMapper :
    EntityMapper<AddressComponents, AddressComponentsDomainEntity> {

    override fun mapHigher(obj: AddressComponents): AddressComponentsDomainEntity {
        return with(obj) {
            AddressComponentsDomainEntity(
                streetAddress = streetAddress,
                locality = locality,
                primaryAdminArea = primaryAdminArea,
                secondaryAdminArea = secondaryAdminArea,
                country = country,
            )
        }
    }

    override fun mapLower(obj: AddressComponentsDomainEntity): AddressComponents {
        return with(obj) {
            AddressComponents(
                streetAddress = streetAddress,
                locality = locality,
                primaryAdminArea = primaryAdminArea,
                secondaryAdminArea = secondaryAdminArea,
                country = country,
            )
        }
    }
}