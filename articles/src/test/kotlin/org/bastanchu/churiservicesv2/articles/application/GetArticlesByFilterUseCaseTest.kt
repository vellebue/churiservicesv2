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
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class GetArticlesByFilterUseCaseTest {

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @InjectMocks
    private lateinit var useCase: GetArticlesByFilterService

    private val articles = listOf(
        Article(
            id = 1L,
            articleId = "ART001",
            articleName = "Coca Cola",
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
            formats = listOf(
                ArticleFormat(
                    id = 2L, description = "Bottle", referenceUnit = true,
                    eanCode = null, eanType = null, saleUnit = true,
                    conversionFactor = BigDecimal.ONE, articleUnitId = "BOT"
                )
            )
        )
    )

    @Test
    fun `should return articles matching filter`() {
        val filter = ArticleFilterDto(articleId = "ART*", articleName = null)
        `when`(articleRepository.findByFilter("ART*", null)).thenReturn(articles)

        val result = useCase.execute(filter)

        assertEquals(2, result.size)
        assertEquals("ART001", result[0].articleId)
        assertEquals("ART002", result[1].articleId)
    }

    @Test
    fun `should return empty list when no match`() {
        val filter = ArticleFilterDto(articleId = "ZZZ*", articleName = null)
        `when`(articleRepository.findByFilter("ZZZ*", null)).thenReturn(emptyList())

        val result = useCase.execute(filter)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return all articles when filter is empty`() {
        val filter = ArticleFilterDto()
        `when`(articleRepository.findByFilter(null, null)).thenReturn(articles)

        val result = useCase.execute(filter)

        assertEquals(2, result.size)
    }
}
