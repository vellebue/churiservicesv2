package org.bastanchu.churiservicesv2.articles.domain

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest

interface PriceConditionRepository {
    fun findById(conditionId: Long): PriceCondition?
    fun findAll(): List<PriceCondition>
    fun save(priceCondition: PriceCondition): PriceCondition
    fun deleteById(conditionId: Long)
    fun findByFilter(filter: PriceConditionFilter, pageRequest: PageRequest): FilteredPage<PriceCondition>
    fun findAllByArticleId(articleId: Long): List<PriceCondition>
}
