package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.infrastructure.persistence.pagination.toFilteredPage
import org.bastanchu.churiservicesv2.common.infrastructure.persistence.pagination.toPageable
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.springframework.stereotype.Repository

@Repository
class CustomerRepositoryImpl(
    private val jpaRepository: CustomerJpaRepository
) : CustomerRepository {

    override fun save(customer: Customer): Customer {
        val entity = if (customer.customerId != null) {
            val existing = jpaRepository.findById(customer.customerId).orElseThrow()
            existing.commercialName = customer.commercialName
            existing.socialName = customer.socialName
            existing.vatNumber = customer.vatNumber
            customer.customerAddress.applyTo(existing.customerAddress!!)
            customer.socialAddress.applyTo(existing.socialAddress!!)
            applyBillingAddress(customer, existing)
            existing
        } else {
            CustomerJpaEntity(
                commercialName = customer.commercialName,
                socialName = customer.socialName,
                vatNumber = customer.vatNumber,
                customerAddress = customer.customerAddress.toJpaEntity(),
                socialAddress = customer.socialAddress.toJpaEntity(),
                billingAddress = customer.billingAddress?.toJpaEntity()
            )
        }
        return jpaRepository.save(entity).toDomain()
    }

    private fun applyBillingAddress(customer: Customer, existing: CustomerJpaEntity) {
        val incoming = customer.billingAddress
        val current = existing.billingAddress
        when {
            incoming == null && current != null -> existing.billingAddress = null
            incoming != null && current == null -> existing.billingAddress = incoming.toJpaEntity()
            incoming != null && current != null -> incoming.applyTo(current)
        }
    }

    override fun findById(id: Long): Customer? =
        jpaRepository.findById(id).orElse(null)?.toDomain()

    override fun deleteById(id: Long) {
        jpaRepository.deleteById(id)
    }

    override fun findByFilter(
        commercialNamePattern: String?,
        socialNamePattern: String?,
        vatNumberPattern: String?,
        pageRequest: PageRequest
    ): FilteredPage<Customer> {
        val results = jpaRepository.findByFilter(
            commercialName = commercialNamePattern?.replace('*', '%'),
            socialName = socialNamePattern?.replace('*', '%'),
            vatNumber = vatNumberPattern?.replace('*', '%'),
            pageable = pageRequest.toPageable()
        )
        return results.toFilteredPage(pageRequest) { it.toDomain(includeDelegations = false) }
    }

    override fun existsByCountryAndVatNumber(country: String, vatNumber: String, excludeCustomerId: Long?): Boolean =
        jpaRepository.existsByCountryAndVatNumber(country, vatNumber, excludeCustomerId)
}
