package org.bastanchu.churiservicesv2.articles.domain

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest

interface PriceConditionRepository {
    fun save(priceCondition: PriceCondition): PriceCondition
    fun findById(conditionId: Long): PriceCondition?
    fun deleteById(conditionId: Long)
    fun findByFilter(
        conditionType: String?,
        descriptionPattern: String?,
        pageRequest: PageRequest
    ): FilteredPage<PriceCondition>
}
