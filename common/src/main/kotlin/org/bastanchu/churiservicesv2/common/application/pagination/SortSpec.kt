package org.bastanchu.churiservicesv2.common.application.pagination

import org.bastanchu.churiservicesv2.common.domain.exception.InvalidPageRequestException
import org.bastanchu.churiservicesv2.common.domain.pagination.SortCriterion
import org.bastanchu.churiservicesv2.common.domain.pagination.SortDirection

class SortSpec(
    private val allowedFields: Set<String>,
    private val default: List<SortCriterion>,
    private val tieBreaker: String,
) {
    init {
        require(allowedFields.isNotEmpty()) { "allowedFields must not be empty" }
        require(tieBreaker in allowedFields) {
            "tieBreaker '$tieBreaker' must be one of the allowed fields"
        }
        require(default.all { it.field in allowedFields }) {
            "All default sort fields must be present in allowedFields"
        }
    }

    fun resolve(requested: List<SortCriterion>): List<SortCriterion> {
        val effective = requested.ifEmpty { default }
        effective.forEach { criterion ->
            if (criterion.field !in allowedFields) {
                throw InvalidPageRequestException(
                    "Unknown sort field '${criterion.field}'. Allowed: ${allowedFields.sorted()}",
                )
            }
        }
        return if (effective.any { it.field == tieBreaker }) {
            effective
        } else {
            effective + SortCriterion(tieBreaker, SortDirection.ASC)
        }
    }
}
