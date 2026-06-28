package org.bastanchu.churiservicesv2.customers.domain.exception

class DelegationNotFoundException(val delegationId: Long) :
    RuntimeException("Customer delegation with id $delegationId not found")
