package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.infrastructure.persistence.pagination.toFilteredPage
import org.bastanchu.churiservicesv2.common.infrastructure.persistence.pagination.toPageable
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.springframework.stereotype.Repository

@Repository
class CustomerDelegationRepositoryImpl(
    private val jpaRepository: CustomerDelegationJpaRepository,
    private val customerJpaRepository: CustomerJpaRepository
) : CustomerDelegationRepository {

    override fun save(delegation: CustomerDelegation): CustomerDelegation {
        val entity = if (delegation.delegationId != null) {
            val existing = jpaRepository.findById(delegation.delegationId).orElseThrow()
            existing.orderId = delegation.orderId
            existing.name = delegation.name
            delegation.address.applyTo(existing.address!!)
            applyBillingAddress(delegation, existing)
            existing
        } else {
            val customer = customerJpaRepository.findById(delegation.customerId).orElseThrow()
            CustomerDelegationJpaEntity(
                customer = customer,
                orderId = delegation.orderId,
                name = delegation.name,
                address = delegation.address.toJpaEntity(),
                billingAddress = delegation.billingAddress?.toJpaEntity()
            )
        }
        return jpaRepository.save(entity).toDomain()
    }

    private fun applyBillingAddress(delegation: CustomerDelegation, existing: CustomerDelegationJpaEntity) {
        val incoming = delegation.billingAddress
        val current = existing.billingAddress
        when {
            incoming == null && current != null -> existing.billingAddress = null
            incoming != null && current == null -> existing.billingAddress = incoming.toJpaEntity()
            incoming != null && current != null -> incoming.applyTo(current)
        }
    }

    override fun findById(id: Long): CustomerDelegation? =
        jpaRepository.findById(id).orElse(null)?.toDomain()

    override fun deleteById(id: Long) {
        jpaRepository.deleteById(id)
    }

    override fun findByFilter(namePattern: String?, pageRequest: PageRequest): FilteredPage<CustomerDelegation> {
        val results = jpaRepository.findByFilter(
            name = namePattern?.replace('*', '%'),
            pageable = pageRequest.toPageable()
        )
        return results.toFilteredPage(pageRequest) { it.toDomain() }
    }

    override fun maxOrderIdForCustomer(customerId: Long): Int? =
        jpaRepository.maxOrderIdForCustomer(customerId)

    override fun existsByCustomerIdAndOrderId(customerId: Long, orderId: Int, excludeDelegationId: Long?): Boolean =
        jpaRepository.existsByCustomerIdAndOrderId(customerId, orderId, excludeDelegationId)
}
