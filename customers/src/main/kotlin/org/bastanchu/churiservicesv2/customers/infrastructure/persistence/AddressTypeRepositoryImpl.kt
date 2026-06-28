package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import org.bastanchu.churiservicesv2.customers.domain.AddressType
import org.bastanchu.churiservicesv2.customers.domain.AddressTypeRepository
import org.springframework.stereotype.Repository

@Repository
class AddressTypeRepositoryImpl(
    private val jpaRepository: AddressTypeJpaRepository
) : AddressTypeRepository {

    override fun findByCode(code: String): AddressType? =
        jpaRepository.findById(code).orElse(null)?.toDomain()

    override fun findAll(): List<AddressType> =
        jpaRepository.findAll().map { it.toDomain() }
}
