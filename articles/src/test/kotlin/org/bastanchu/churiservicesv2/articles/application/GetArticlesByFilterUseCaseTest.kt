package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.ArticleFilterDto
import org.bastanchu.churiservicesv2.articles.application.service.GetArticlesByFilterService
import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class GetArticlesByFilterUseCaseTest {

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @InjectMocks
    private lateinit var useCase: GetArticlesByFilterService

    private val beginValidity = LocalDate.of(2026, 1, 1)
    private val endValidity = LocalDate.of(2026, 12, 31)

    private val articles = listOf(
        Article(
            id = 1L,
            articleId = "ART001",
            articleName = "Coca Cola",
            beginValidityDate = beginValidity,
            endValidityDate = endValidity,
            formats = listOf(
                ArticleFormat(
                    id = 1L, description = "Unit", referenceUnit = true,
                    eanCode = null, eanType = null, saleUnit = true,
                    conversionFactor = BigDecimal.ONE, articleUnitId = "UN"
                )
            )
        ),
        Article(
            id = 2L,
            articleId = "ART002",
            articleName = "Coca Cola Zero",
            beginValidityDate = beginValidity,
            endValidityDate = null,
            formats = listOf(
                ArticleFormat(
                    id = 2L, description = "Bottle", referenceUnit = true,
                    eanCode = null, eanType = null, saleUnit = true,
                    conversionFactor = BigDecimal.ONE, articleUnitId = "BOT"
                )
            )
        ))

    @Test
    fun `should return articles matching filter`() {
        val filter = ArticleFilterDto(articleId = "ART*", articleName = null)
        whenever(articleRepository.findByFilter(eq("ART*"), eq(null), any()))
            .thenReturn(articles)

        val result = useCase.execute(filter)

        assertEquals(2, result.size)
        assertEquals("ART001", result[0].articleId)
        assertEquals("ART002", result[1].articleId)
    }

    @Test
    fun `should return empty list when no match`() {
        val filter = ArticleFilterDto(articleId = "ZZZ*", articleName = null)
        whenever(articleRepository.findByFilter(eq("ZZZ*"), eq(null), any())).thenReturn(emptyList())

        val result = useCase.execute(filter)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return all articles when filter is empty`() {
        val filter = ArticleFilterDto()
        whenever(articleRepository.findByFilter(eq(null), eq(null), any())).thenReturn(articles)

        val result = useCase.execute(filter)

        assertEquals(2, result.size)
    }

    @Test
    fun `should pass current date as reference for validity filtering`() {
        val filter = ArticleFilterDto()
        whenever(articleRepository.findByFilter(eq(null), eq(null), any<LocalDate>()))
            .thenReturn(emptyList())

        useCase.execute(filter)

        verify(articleRepository).findByFilter(eq(null), eq(null), eq(LocalDate.now()))
    }
}
