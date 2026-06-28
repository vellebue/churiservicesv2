package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.GetDelegationByIdUseCase
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.DelegationNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetDelegationByIdService(
    private val delegationRepository: CustomerDelegationRepository
) : GetDelegationByIdUseCase {

    override fun execute(delegationId: Long): CustomerDelegationDto {
        val delegation = delegationRepository.findById(delegationId)
            ?: throw DelegationNotFoundException(delegationId)
        return delegation.toDto()
    }
}
