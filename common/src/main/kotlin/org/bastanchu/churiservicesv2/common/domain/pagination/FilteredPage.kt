package org.bastanchu.churiservicesv2.common.domain.pagination

data class FilteredPage<T>(
    val content: List<T>,
    val offset: Long,
    val limit: Int,
    val hasMore: Boolean,
    val sort: List<SortCriterion>,
)
