package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.AddressType
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation

internal fun AddressJpaEntity.toDomain(): Address = Address(
    addressId = addressId,
    addressType = addressType,
    name = name,
    address = address,
    postalCode = postalCode,
    city = city,
    country = country,
    region = region
)

internal fun AddressTypeJpaEntity.toDomain(): AddressType = AddressType(
    addressType = addressType,
    description = description,
    addressKey = addressKey
)

internal fun Address.toJpaEntity(): AddressJpaEntity = AddressJpaEntity(
    addressId = addressId ?: 0,
    addressType = addressType,
    name = name,
    address = address,
    postalCode = postalCode,
    city = city,
    country = country,
    region = region
)

internal fun Address.applyTo(entity: AddressJpaEntity) {
    entity.addressType = addressType
    entity.name = name
    entity.address = address
    entity.postalCode = postalCode
    entity.city = city
    entity.country = country
    entity.region = region
}

internal fun CustomerJpaEntity.toDomain(includeDelegations: Boolean = true): Customer = Customer(
    customerId = customerId,
    commercialName = commercialName,
    socialName = socialName,
    vatNumber = vatNumber,
    customerAddress = customerAddress!!.toDomain(),
    socialAddress = socialAddress!!.toDomain(),
    billingAddress = billingAddress?.toDomain(),
    delegations = if (includeDelegations) delegations.map { it.toDomain() } else emptyList()
)

internal fun CustomerDelegationJpaEntity.toDomain(): CustomerDelegation = CustomerDelegation(
    delegationId = delegationId,
    customerId = customer!!.customerId,
    orderId = orderId,
    name = name,
    address = address!!.toDomain(),
    billingAddress = billingAddress?.toDomain()
)
