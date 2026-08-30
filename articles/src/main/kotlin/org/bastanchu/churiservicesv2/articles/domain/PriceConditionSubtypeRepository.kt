package org.bastanchu.churiservicesv2.articles.domain

interface PriceConditionSubtypeRepository {
    fun findById(subtypeId: Long): PriceConditionSubtype?
    fun findByMnemonic(conditionType: String, conditionSubtype: String): PriceConditionSubtype?
    fun findByConditionType(conditionType: String): List<PriceConditionSubtype>
    fun findAll(): List<PriceConditionSubtype>
}
