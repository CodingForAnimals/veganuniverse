package org.codingforanimals.veganuniverse.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.EntityMapper
import org.codingforanimals.veganuniverse.services.firebase.entity.Period
import org.codingforanimals.veganuniverse.places.entity.Period as PeriodDomainEntity

internal class PeriodMapper : EntityMapper<Period, PeriodDomainEntity> {

    override fun mapHigher(obj: Period): PeriodDomainEntity {
        return with(obj) {
            PeriodDomainEntity(
                openingHour = openingHour,
                openingMinute = openingMinute,
                closingMinute = closingMinute
            )
        }
    }

    override fun mapLower(obj: PeriodDomainEntity): Period {
        return with(obj) {
            Period(
                openingHour = openingHour,
                openingMinute = openingMinute,
                closingHour = closingHour,
                closingMinute = closingMinute
            )
        }
    }
}