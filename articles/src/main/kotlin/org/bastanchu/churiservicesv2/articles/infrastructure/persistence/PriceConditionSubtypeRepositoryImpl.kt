package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.bastanchu.churiservicesv2.articles.domain.PriceConditionSubtype
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionSubtypeRepository
import org.springframework.stereotype.Repository

@Repository
class PriceConditionSubtypeRepositoryImpl(
    private val jpaRepository: PriceConditionSubtypeJpaRepository
) : PriceConditionSubtypeRepository {

    override fun findById(subtypeId: Long): PriceConditionSubtype? {
        return jpaRepository.findById(subtypeId).orElse(null)?.toDomain()
    }

    override fun findByMnemonic(conditionType: String, conditionSubtype: String): PriceConditionSubtype? {
        return jpaRepository.findByConditionTypeAndConditionSubtype(conditionType, conditionSubtype)?.toDomain()
    }

    override fun findByConditionType(conditionType: String): List<PriceConditionSubtype> {
        return jpaRepository.findByConditionType(conditionType).map { it.toDomain() }
    }

    override fun findAll(): List<PriceConditionSubtype> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    private fun PriceConditionSubtypeJpaEntity.toDomain(): PriceConditionSubtype {
        return PriceConditionSubtype(
            subtypeId = subtypeId,
            conditionType = conditionType,
            conditionSubtype = conditionSubtype,
            description = description,
            key = subtypeKey
        )
    }
}
