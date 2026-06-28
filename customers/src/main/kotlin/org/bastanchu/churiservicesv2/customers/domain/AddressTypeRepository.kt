package org.bastanchu.churiservicesv2.customers.domain

interface AddressTypeRepository {
    fun findByCode(code: String): AddressType?
    fun findAll(): List<AddressType>
}
