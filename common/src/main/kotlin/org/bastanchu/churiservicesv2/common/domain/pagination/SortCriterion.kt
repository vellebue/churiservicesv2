package org.bastanchu.churiservicesv2.common.domain.pagination

data class SortCriterion(val field: String, val direction: SortDirection) {
    override fun toString(): String = "$field,${direction.name.lowercase()}"
}
