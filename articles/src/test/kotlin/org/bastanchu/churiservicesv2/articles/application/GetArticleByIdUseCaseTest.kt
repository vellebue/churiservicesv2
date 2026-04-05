package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.service.GetArticleByIdService
import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.ArticleNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class GetArticleByIdUseCaseTest {

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @InjectMocks
    private lateinit var useCase: GetArticleByIdService

    private val existingArticle = Article(
        id = 1L,
        articleId = "ART001",
        articleName = "Test Article",
        formats = listOf(
            ArticleFormat(
                id = 1L, description = "Unit", referenceUnit = true,
                eanCode = "1234567890123", eanType = "EAN13", saleUnit = true,
                conversionFactor = BigDecimal.ONE, articleUnitId = "UN"
            )
        )
    )

    @Test
    fun `should return article when found`() {
        `when`(articleRepository.findById(1L)).thenReturn(existingArticle)

        val result = useCase.execute(1L)

        assertEquals(1L, result.id)
        assertEquals("ART001", result.articleId)
        assertEquals("Test Article", result.articleName)
        assertEquals(1, result.formats.size)
        assertEquals("UN", result.formats[0].articleUnitId)
    }

    @Test
    fun `should throw when article not found`() {
        `when`(articleRepository.findById(99L)).thenReturn(null)

        assertThrows(ArticleNotFoundException::class.java) {
            useCase.execute(99L)
        }
    }
}
