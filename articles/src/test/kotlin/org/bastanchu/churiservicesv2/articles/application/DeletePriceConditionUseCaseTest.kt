package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.service.DeletePriceConditionService
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.PriceConditionNotFoundException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class DeletePriceConditionUseCaseTest {

    @Mock
    private lateinit var priceConditionRepository: PriceConditionRepository

    @InjectMocks
    private lateinit var useCase: DeletePriceConditionService

    @Test
    fun `should delete price condition when it exists`() {
        val priceCondition = PriceCondition(
            conditionId = 3L,
            subtypeId = 10L,
            scope = 'L',
            evaluation = 'D',
            description = "d"
        )
        whenever(priceConditionRepository.findById(3L)).thenReturn(priceCondition)

        useCase.execute(3L)

        verify(priceConditionRepository).deleteById(3L)
    }

    @Test
    fun `should throw when price condition does not exist`() {
        whenever(priceConditionRepository.findById(3L)).thenReturn(null)

        assertThrows(PriceConditionNotFoundException::class.java) {
            useCase.execute(3L)
        }
    }
}
