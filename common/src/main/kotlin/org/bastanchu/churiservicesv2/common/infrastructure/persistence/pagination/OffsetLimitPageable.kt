package org.bastanchu.churiservicesv2.common.infrastructure.persistence.pagination

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class OffsetLimitPageable(
    private val offset: Long,
    private val limit: Int,
    private val sort: Sort,
) : Pageable {
    override fun getPageNumber(): Int = if (limit == 0) 0 else (offset / limit).toInt()
    override fun getPageSize(): Int = limit
    override fun getOffset(): Long = offset
    override fun getSort(): Sort = sort
    override fun next(): Pageable = OffsetLimitPageable(offset + limit, limit, sort)
    override fun previousOrFirst(): Pageable =
        if (hasPrevious()) OffsetLimitPageable((offset - limit).coerceAtLeast(0), limit, sort) else first()
    override fun first(): Pageable = OffsetLimitPageable(0, limit, sort)
    override fun withPage(pageNumber: Int): Pageable =
        OffsetLimitPageable(pageNumber.toLong() * limit, limit, sort)
    override fun hasPrevious(): Boolean = offset > 0
    override fun isPaged(): Boolean = true
    override fun isUnpaged(): Boolean = false
}
