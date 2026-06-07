package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.service.GetRegionsByCountryService
import org.bastanchu.churiservicesv2.articles.domain.CountryRegion
import org.bastanchu.churiservicesv2.articles.domain.CountryRegionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.eq
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.MessageSource

@ExtendWith(MockitoExtension::class)
class GetRegionsByCountryUseCaseTest {

    @Mock
    private lateinit var countryRegionRepository: CountryRegionRepository

    @Mock
    private lateinit var messageSource: MessageSource

    @InjectMocks
    private lateinit var useCase: GetRegionsByCountryService

    @Test
    fun `should return localized regions for country`() {
        val regions = listOf(
            CountryRegion(countryId = "ES", regionId = "MD", description = "Madrid", regionKey = "region.ES.MD"),
            CountryRegion(countryId = "ES", regionId = "CT", description = "Catalonia", regionKey = "region.ES.CT")
        )
        `when`(countryRegionRepository.findByCountryId("ES")).thenReturn(regions)
        `when`(messageSource.getMessage(eq("region.ES.MD"), eq(null), any())).thenReturn("Madrid")
        `when`(messageSource.getMessage(eq("region.ES.CT"), eq(null), any())).thenReturn("Cataluña")

        val result = useCase.execute("ES")

        assertEquals(2, result.size)
        assertEquals("MD", result[0].regionId)
        assertEquals("ES", result[0].countryId)
        assertEquals("Madrid", result[0].localizedDescription)
        assertEquals("Cataluña", result[1].localizedDescription)
    }

    @Test
    fun `should return empty list when country has no regions`() {
        `when`(countryRegionRepository.findByCountryId("ZZ")).thenReturn(emptyList())

        val result = useCase.execute("ZZ")

        assertTrue(result.isEmpty())
    }
}
