package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.command.PriceConditionValueCommand
import org.bastanchu.churiservicesv2.articles.domain.exception.InvalidPriceConditionException

internal object PriceConditionValidator {

    fun validateValues(values: List<PriceConditionValueCommand>) {
        if (values.isEmpty()) {
            throw InvalidPriceConditionException("A price condition must have at least one value")
        }
        validateNoIntervalOverlap(values)
    }

    fun requireAtLeastOneOpenEnded(values: List<PriceConditionValueCommand>) {
        if (values.none { it.endDate == null }) {
            throw InvalidPriceConditionException(
                "A price condition must have at least one value with empty endDate"
            )
        }
    }

    private fun validateNoIntervalOverlap(values: List<PriceConditionValueCommand>) {
        val sorted = values.sortedBy { it.beginDate }
        for (i in sorted.indices) {
            val a = sorted[i]
            if (a.endDate != null && a.endDate.isBefore(a.beginDate)) {
                throw InvalidPriceConditionException(
                    "endDate ${a.endDate} must be on or after beginDate ${a.beginDate}"
                )
            }
            for (j in i + 1 until sorted.size) {
                val b = sorted[j]
                val aEnd = a.endDate
                val overlaps = aEnd == null || !b.beginDate.isAfter(aEnd)
                if (overlaps) {
                    throw InvalidPriceConditionException(
                        "Price condition values have overlapping validity intervals"
                    )
                }
            }
        }
    }
}
