package org.bastanchu.churiservicesv2.common.infrastructure.persistence.pagination

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.domain.pagination.SortCriterion
import org.bastanchu.churiservicesv2.common.domain.pagination.SortDirection
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

// SQL LIMIT is intentionally `limit + 1` to detect whether more rows exist
// without issuing a second COUNT query. The extra row is trimmed in toFilteredPage.
fun PageRequest.toPageable(): Pageable {
    val springSort = if (sort.isEmpty()) Sort.unsorted() else Sort.by(sort.map { it.toSpringOrder() })
    return OffsetLimitPageable(offset = offset, limit = limit + 1, sort = springSort)
}

fun <E, T> List<E>.toFilteredPage(
    request: PageRequest,
    mapper: (E) -> T,
): FilteredPage<T> {
    val hasMore = size > request.limit
    val items = if (hasMore) take(request.limit) else this
    return FilteredPage(
        content = items.map(mapper),
        offset = request.offset,
        limit = request.limit,
        hasMore = hasMore,
        sort = request.sort,
    )
}

private fun SortCriterion.toSpringOrder(): Sort.Order = when (direction) {
    SortDirection.ASC -> Sort.Order.asc(field)
    SortDirection.DESC -> Sort.Order.desc(field)
}
