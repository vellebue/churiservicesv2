package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.bastanchu.churiservicesv2.articles.domain.PriceConditionType
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionTypeRepository
import org.springframework.stereotype.Repository

@Repository
class PriceConditionTypeRepositoryImpl(
    private val jpaRepository: PriceConditionTypeJpaRepository
) : PriceConditionTypeRepository {

    override fun findByConditionType(conditionType: String): PriceConditionType? {
        return jpaRepository.findById(conditionType).orElse(null)?.toDomain()
    }

    override fun findAll(): List<PriceConditionType> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    private fun PriceConditionTypeJpaEntity.toDomain(): PriceConditionType = PriceConditionType(
        conditionType = conditionType,
        description = description,
        key = key
    )
}
