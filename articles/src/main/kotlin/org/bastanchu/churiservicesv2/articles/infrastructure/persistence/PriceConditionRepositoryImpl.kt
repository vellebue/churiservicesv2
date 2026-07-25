package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.infrastructure.persistence.pagination.toFilteredPage
import org.bastanchu.churiservicesv2.common.infrastructure.persistence.pagination.toPageable
import org.springframework.stereotype.Repository

@Repository
class PriceConditionRepositoryImpl(
    private val jpaRepository: PriceConditionJpaRepository
) : PriceConditionRepository {

    override fun save(priceCondition: PriceCondition): PriceCondition {
        val jpaEntity = if (priceCondition.conditionId != null) {
            val existing = jpaRepository.findById(priceCondition.conditionId).orElseThrow()
            existing.conditionType = priceCondition.conditionType
            existing.description = priceCondition.description
            existing.scope = priceCondition.scope
            existing.evaluation = priceCondition.evaluation
            existing.availability = priceCondition.availability
            existing.numOrder = priceCondition.numOrder
            existing.values.clear()
            priceCondition.values.forEach { v ->
                existing.values.add(v.toJpaEntity(existing))
            }
            existing
        } else {
            val newEntity = PriceConditionJpaEntity(
                conditionType = priceCondition.conditionType,
                description = priceCondition.description,
                scope = priceCondition.scope,
                evaluation = priceCondition.evaluation,
                availability = priceCondition.availability,
                numOrder = priceCondition.numOrder
            )
            priceCondition.values.forEach { v ->
                newEntity.values.add(v.toJpaEntity(newEntity))
            }
            newEntity
        }

        val saved = jpaRepository.save(jpaEntity)
        return saved.toDomain()
    }

    override fun findById(conditionId: Long): PriceCondition? {
        return jpaRepository.findById(conditionId).orElse(null)?.toDomain()
    }

    override fun deleteById(conditionId: Long) {
        jpaRepository.deleteById(conditionId)
    }

    override fun findByFilter(
        conditionType: String?,
        descriptionPattern: String?,
        pageRequest: PageRequest
    ): FilteredPage<PriceCondition> {
        val results = jpaRepository.findByFilter(
            conditionType = conditionType,
            description = descriptionPattern,
            pageable = pageRequest.toPageable()
        )
        return results.toFilteredPage(pageRequest) { it.toDomain() }
    }

    private fun PriceConditionJpaEntity.toDomain(): PriceCondition = PriceCondition(
        conditionId = conditionId,
        conditionType = conditionType,
        description = description,
        scope = scope,
        evaluation = evaluation,
        availability = availability,
        numOrder = numOrder,
        values = values.map { it.toDomain() }
    )

    private fun PriceConditionValueJpaEntity.toDomain(): PriceConditionValue = PriceConditionValue(
        valueId = valueId,
        type = type,
        value = value,
        beginDate = beginDate,
        endDate = endDate
    )

    private fun PriceConditionValue.toJpaEntity(parent: PriceConditionJpaEntity): PriceConditionValueJpaEntity =
        PriceConditionValueJpaEntity(
            priceCondition = parent,
            type = type,
            value = value,
            beginDate = beginDate,
            endDate = endDate
        )
}
