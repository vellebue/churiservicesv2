package org.bastanchu.churiservicesv2.common.domain.pagination

import org.bastanchu.churiservicesv2.common.domain.exception.InvalidPageRequestException

data class PageRequest(
    val offset: Long,
    val limit: Int,
    val sort: List<SortCriterion>,
) {
    init {
        if (offset < 0) {
            throw InvalidPageRequestException("offset must be >= 0 (was $offset)")
        }
        if (limit !in 1..PaginationLimits.MAX_LIMIT) {
            throw InvalidPageRequestException(
                "limit must be between 1 and ${PaginationLimits.MAX_LIMIT} (was $limit)",
            )
        }
    }

    companion object {
        fun defaults(): PageRequest = PageRequest(
            offset = PaginationLimits.DEFAULT_OFFSET,
            limit = PaginationLimits.DEFAULT_LIMIT,
            sort = emptyList(),
        )
    }
}
