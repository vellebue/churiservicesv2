package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.DeleteDelegationUseCase
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.DelegationNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteDelegationService(
    private val delegationRepository: CustomerDelegationRepository
) : DeleteDelegationUseCase {

    override fun execute(delegationId: Long) {
        delegationRepository.findById(delegationId) ?: throw DelegationNotFoundException(delegationId)
        delegationRepository.deleteById(delegationId)
    }
}
