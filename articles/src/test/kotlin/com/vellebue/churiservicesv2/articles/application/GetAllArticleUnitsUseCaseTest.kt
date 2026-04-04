package com.vellebue.churiservicesv2.articles.application

import com.vellebue.churiservicesv2.articles.application.service.GetAllArticleUnitsService
import com.vellebue.churiservicesv2.articles.domain.ArticleUnit
import com.vellebue.churiservicesv2.articles.domain.ArticleUnitRepository
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
class GetAllArticleUnitsUseCaseTest {

    @Mock
    private lateinit var articleUnitRepository: ArticleUnitRepository

    @Mock
    private lateinit var messageSource: MessageSource

    @InjectMocks
    private lateinit var useCase: GetAllArticleUnitsService

    @Test
    fun `should return all article units`() {
        val units = listOf(
            ArticleUnit(symbol = "UN", description = "Unit", translationKey = "article.unit.un"),
            ArticleUnit(symbol = "BOT", description = "Bottle", translationKey = "article.unit.bot")
        )
        `when`(articleUnitRepository.findAll()).thenReturn(units)
        `when`(messageSource.getMessage(eq("article.unit.un"), eq(null), any())).thenReturn("Unit")
        `when`(messageSource.getMessage(eq("article.unit.bot"), eq(null), any())).thenReturn("Bottle")

        val result = useCase.execute()

        assertEquals(2, result.size)
        assertEquals("UN", result[0].symbol)
        assertEquals("BOT", result[1].symbol)
        assertEquals("Unit", result[0].localizedDescription)
        assertEquals("Bottle", result[1].localizedDescription)
    }

    @Test
    fun `should return empty list when no article units exist`() {
        `when`(articleUnitRepository.findAll()).thenReturn(emptyList())

        val result = useCase.execute()

        assertTrue(result.isEmpty())
    }
}
