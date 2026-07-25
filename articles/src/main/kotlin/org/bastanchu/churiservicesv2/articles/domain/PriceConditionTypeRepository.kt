package org.bastanchu.churiservicesv2.articles.domain

interface PriceConditionTypeRepository {
    fun findByConditionType(conditionType: String): PriceConditionType?
    fun findAll(): List<PriceConditionType>
}
