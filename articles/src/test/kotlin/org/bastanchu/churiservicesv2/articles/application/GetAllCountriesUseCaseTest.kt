package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.service.GetAllCountriesService
import org.bastanchu.churiservicesv2.articles.domain.Country
import org.bastanchu.churiservicesv2.articles.domain.CountryRepository
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
class GetAllCountriesUseCaseTest {

    @Mock
    private lateinit var countryRepository: CountryRepository

    @Mock
    private lateinit var messageSource: MessageSource

    @InjectMocks
    private lateinit var useCase: GetAllCountriesService

    @Test
    fun `should return all countries with localized names`() {
        val countries = listOf(
            Country(countryId = "ES", description = "Spain", countryKey = "country.ES"),
            Country(countryId = "DE", description = "Germany", countryKey = "country.DE")
        )
        `when`(countryRepository.findAll()).thenReturn(countries)
        `when`(messageSource.getMessage(eq("country.ES"), eq(null), any())).thenReturn("España")
        `when`(messageSource.getMessage(eq("country.DE"), eq(null), any())).thenReturn("Alemania")

        val result = useCase.execute()

        assertEquals(2, result.size)
        assertEquals("ES", result[0].countryId)
        assertEquals("Spain", result[0].description)
        assertEquals("España", result[0].localizedDescription)
        assertEquals("DE", result[1].countryId)
        assertEquals("Alemania", result[1].localizedDescription)
    }

    @Test
    fun `should return empty list when no countries exist`() {
        `when`(countryRepository.findAll()).thenReturn(emptyList())

        val result = useCase.execute()

        assertTrue(result.isEmpty())
    }
}
