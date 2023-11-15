package org.codingforanimals.veganuniverse.places.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.EntityMapper
import org.codingforanimals.veganuniverse.places.services.firebase.entity.OpeningHours
import org.codingforanimals.veganuniverse.places.services.firebase.entity.Period
import org.codingforanimals.veganuniverse.places.entity.OpeningHours as OpeningHoursDomainEntity
import org.codingforanimals.veganuniverse.places.entity.Period as PeriodDomainEntity

internal class OpeningHoursMapper(
    private val periodMapper: EntityMapper<Period, PeriodDomainEntity>,
) : EntityMapper<OpeningHours, OpeningHoursDomainEntity> {

    override fun mapHigher(obj: OpeningHours): OpeningHoursDomainEntity {
        return with(obj) {
            OpeningHoursDomainEntity(
                dayOfWeek = dayOfWeek,
                mainPeriod = mainPeriod?.let { periodMapper.mapHigher(it) },
                secondaryPeriod = secondaryPeriod?.let { periodMapper.mapHigher(it) },
            )
        }
    }

    override fun mapLower(obj: OpeningHoursDomainEntity): OpeningHours {
        return with(obj) {
            OpeningHours(
                dayOfWeek = dayOfWeek,
                mainPeriod = mainPeriod?.let { periodMapper.mapLower(it) },
                secondaryPeriod = secondaryPeriod?.let { periodMapper.mapLower(it) },
            )
        }
    }
}