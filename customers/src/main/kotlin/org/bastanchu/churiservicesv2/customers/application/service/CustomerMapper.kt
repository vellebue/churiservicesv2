package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.command.AddressCommand
import org.bastanchu.churiservicesv2.customers.application.dto.AddressDto
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation

fun Address.toDto(): AddressDto = AddressDto(
    addressId = addressId!!,
    addressType = addressType,
    name = name,
    address = address,
    postalCode = postalCode,
    city = city,
    country = country,
    region = region
)

fun Customer.toDto(): CustomerDto = CustomerDto(
    customerId = customerId!!,
    commercialName = commercialName,
    socialName = socialName,
    vatNumber = vatNumber,
    customerAddress = customerAddress.toDto(),
    socialAddress = socialAddress.toDto(),
    billingAddress = billingAddress?.toDto(),
    delegations = delegations.map { it.toDto() }
)

fun CustomerDelegation.toDto(): CustomerDelegationDto = CustomerDelegationDto(
    delegationId = delegationId!!,
    customerId = customerId,
    orderId = orderId,
    name = name,
    address = address.toDto(),
    billingAddress = billingAddress?.toDto()
)

fun AddressCommand.toDomain(addressType: String): Address = Address(
    addressType = addressType,
    name = name,
    address = address,
    postalCode = postalCode,
    city = city,
    country = country,
    region = region
)
