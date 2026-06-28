package org.bastanchu.churiservicesv2.customers.domain.exception

class CustomerNotFoundException(val customerId: Long) :
    RuntimeException("Customer with id $customerId not found")
