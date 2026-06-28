package org.bastanchu.churiservicesv2.common.infrastructure.web.pagination

import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.bastanchu.churiservicesv2.common.domain.exception.InvalidPageRequestException
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.domain.pagination.PaginationLimits
import org.bastanchu.churiservicesv2.common.domain.pagination.SortCriterion
import org.bastanchu.churiservicesv2.common.domain.pagination.SortDirection

data class PageQueryParams(
    @field:Parameter(description = "0-based index of the first item to return.", example = "0")
    @field:Min(0)
    val offset: Long? = null,

    @field:Parameter(
        description = "Maximum number of items to return. Default 20, max ${PaginationLimits.MAX_LIMIT}.",
        example = "20",
    )
    @field:Min(1)
    @field:Max(PaginationLimits.MAX_LIMIT.toLong())
    val limit: Int? = null,

    @field:Parameter(
        description = "Sort criteria as 'field,(asc|desc)'. Direction defaults to 'asc' when omitted. Repeatable.",
        example = "commercialName,asc",
    )
    val sort: List<String>? = null,
) {
    fun toPageRequest(): PageRequest = PageRequest(
        offset = offset ?: PaginationLimits.DEFAULT_OFFSET,
        limit = limit ?: PaginationLimits.DEFAULT_LIMIT,
        sort = sort.orEmpty().map { it.toSortCriterion() },
    )

    private fun String.toSortCriterion(): SortCriterion {
        val parts = split(",").map { it.trim() }
        if (parts.isEmpty() || parts[0].isBlank() || parts.size > 2) {
            throw InvalidPageRequestException(
                "Malformed sort entry '$this'. Expected 'field' or 'field,(asc|desc)'.",
            )
        }
        val direction = if (parts.size == 2 && parts[1].isNotBlank()) parseDirection(parts[1]) else SortDirection.ASC
        return SortCriterion(parts[0], direction)
    }

    private fun parseDirection(raw: String): SortDirection = when (raw.lowercase()) {
        "asc" -> SortDirection.ASC
        "desc" -> SortDirection.DESC
        else -> throw InvalidPageRequestException(
            "Unknown sort direction '$raw'. Expected 'asc' or 'desc'.",
        )
    }
}
