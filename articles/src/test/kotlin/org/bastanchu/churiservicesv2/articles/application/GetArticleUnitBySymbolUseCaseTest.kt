package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.service.GetArticleUnitBySymbolService
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnit
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnitRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
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
class GetArticleUnitBySymbolUseCaseTest {

    @Mock
    private lateinit var articleUnitRepository: ArticleUnitRepository

    @Mock
    private lateinit var messageSource: MessageSource

    @InjectMocks
    private lateinit var useCase: GetArticleUnitBySymbolService

    @Test
    fun `should return article unit when symbol exists`() {
        val articleUnit = ArticleUnit(symbol = "UN", description = "Unit", translationKey = "article.unit.un")
        `when`(articleUnitRepository.findBySymbol("UN")).thenReturn(articleUnit)
        `when`(messageSource.getMessage(eq("article.unit.un"), eq(null), any())).thenReturn("Unit")

        val result = useCase.execute("UN")

        assertEquals("UN", result?.symbol)
        assertEquals("Unit", result?.description)
        assertEquals("Unit", result?.localizedDescription)
    }

    @Test
    fun `should return null when symbol does not exist`() {
        `when`(articleUnitRepository.findBySymbol("xyz")).thenReturn(null)

        val result = useCase.execute("xyz")

        assertNull(result)
    }
}
