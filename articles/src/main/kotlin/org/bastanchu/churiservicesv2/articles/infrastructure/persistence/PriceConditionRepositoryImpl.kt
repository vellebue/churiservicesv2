package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionFilter
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

    override fun findById(conditionId: Long): PriceCondition? {
        return jpaRepository.findById(conditionId).orElse(null)?.toDomain()
    }

    override fun findAll(): List<PriceCondition> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    override fun save(priceCondition: PriceCondition): PriceCondition {
        val jpaEntity = if (priceCondition.conditionId != null) {
            val existing = jpaRepository.findById(priceCondition.conditionId).orElseThrow()
            existing.subtypeId = priceCondition.subtypeId
            existing.scope = priceCondition.scope
            existing.evaluation = priceCondition.evaluation
            existing.description = priceCondition.description
            existing.values.clear()
            priceCondition.values.forEach { existing.values.add(it.toJpaEntity(existing)) }
            existing
        } else {
            val newEntity = PriceConditionJpaEntity(
                subtypeId = priceCondition.subtypeId,
                scope = priceCondition.scope,
                evaluation = priceCondition.evaluation,
                description = priceCondition.description
            )
            priceCondition.values.forEach { newEntity.values.add(it.toJpaEntity(newEntity)) }
            newEntity
        }
        return jpaRepository.save(jpaEntity).toDomain()
    }

    override fun deleteById(conditionId: Long) {
        jpaRepository.deleteById(conditionId)
    }

    override fun findByFilter(filter: PriceConditionFilter, pageRequest: PageRequest): FilteredPage<PriceCondition> {
        val descriptionPattern = filter.descriptionPattern?.replace('*', '%')
        val rows = jpaRepository.findByFilter(
            subtypeId = filter.subtypeId,
            scope = filter.scope,
            evaluation = filter.evaluation,
            descriptionPattern = descriptionPattern,
            beginDate = filter.beginDate,
            endDate = filter.endDate,
            pageable = pageRequest.toPageable()
        )
        return rows.toFilteredPage(pageRequest) { it.toDomain() }
    }

    private fun PriceConditionJpaEntity.toDomain(): PriceCondition = PriceCondition(
        conditionId = conditionId,
        subtypeId = subtypeId,
        scope = scope,
        evaluation = evaluation,
        description = description,
        values = values.map { it.toDomain() }
    )

    private fun PriceConditionValueJpaEntity.toDomain(): PriceConditionValue = PriceConditionValue(
        valueId = valueId,
        conditionId = priceCondition?.conditionId ?: 0,
        valueType = valueType,
        value = value,
        beginDate = beginDate,
        endDate = endDate
    )

    private fun PriceConditionValue.toJpaEntity(parent: PriceConditionJpaEntity): PriceConditionValueJpaEntity =
        PriceConditionValueJpaEntity(
            priceCondition = parent,
            valueType = valueType,
            value = value,
            beginDate = beginDate,
            endDate = endDate
        )
}
