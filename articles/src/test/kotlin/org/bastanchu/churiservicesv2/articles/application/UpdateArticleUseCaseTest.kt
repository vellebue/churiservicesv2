package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.ArticleFormatCommand
import org.bastanchu.churiservicesv2.articles.application.command.UpdateArticleCommand
import org.bastanchu.churiservicesv2.articles.application.service.UpdateArticleService
import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnit
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnitRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.ArticleNotFoundException
import org.bastanchu.churiservicesv2.articles.domain.exception.InvalidArticleException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class UpdateArticleUseCaseTest {

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @Mock
    private lateinit var articleUnitRepository: ArticleUnitRepository

    @InjectMocks
    private lateinit var useCase: UpdateArticleService

    private val existingArticle = Article(
        id = 1L,
        articleId = "ART001",
        articleName = "Old Name",
        formats = listOf(
            ArticleFormat(
                id = 1L, description = "Unit", referenceUnit = true,
                eanCode = null, eanType = null, saleUnit = true,
                conversionFactor = BigDecimal.ONE, articleUnitId = "UN"
            )
        )
    )

    private val validFormatCommand = ArticleFormatCommand(
        description = "Box",
        referenceUnit = true,
        eanCode = "9876543210123",
        eanType = "EAN13",
        saleUnit = true,
        conversionFactor = BigDecimal.TEN,
        articleUnitId = "BX"
    )

    private val validCommand = UpdateArticleCommand(
        articleId = "ART001",
        articleName = "Updated Name",
        formats = listOf(validFormatCommand)
    )

    @Test
    fun `should update article with valid formats`() {
        val updatedArticle = Article(
            id = 1L,
            articleId = "ART001",
            articleName = "Updated Name",
            formats = listOf(
                ArticleFormat(
                    id = 2L, description = "Box", referenceUnit = true,
                    eanCode = "9876543210123", eanType = "EAN13", saleUnit = true,
                    conversionFactor = BigDecimal.TEN, articleUnitId = "BX"
                )
            )
        )

        `when`(articleRepository.findById(1L)).thenReturn(existingArticle)
        `when`(articleUnitRepository.findBySymbol("BX"))
            .thenReturn(ArticleUnit(symbol = "BX", description = "Box", translationKey = "article.unit.bx"))
        whenever(articleRepository.save(any())).thenReturn(updatedArticle)

        val result = useCase.execute(1L, validCommand)

        assertEquals("Updated Name", result.articleName)
        assertEquals(1, result.formats.size)
        assertEquals("Box", result.formats[0].description)
    }

    @Test
    fun `should throw when article does not exist`() {
        `when`(articleRepository.findById(99L)).thenReturn(null)

        assertThrows(ArticleNotFoundException::class.java) {
            useCase.execute(99L, validCommand)
        }
    }

    @Test
    fun `should throw when no format is configured as reference unit`() {
        val command = validCommand.copy(
            formats = listOf(validFormatCommand.copy(referenceUnit = false))
        )

        `when`(articleRepository.findById(1L)).thenReturn(existingArticle)

        assertThrows(InvalidArticleException::class.java) {
            useCase.execute(1L, command)
        }
    }

    @Test
    fun `should throw when article unit does not exist`() {
        val command = validCommand.copy(
            formats = listOf(validFormatCommand.copy(articleUnitId = "INVALID"))
        )

        `when`(articleRepository.findById(1L)).thenReturn(existingArticle)
        `when`(articleUnitRepository.findBySymbol("INVALID")).thenReturn(null)

        assertThrows(InvalidArticleException::class.java) {
            useCase.execute(1L, command)
        }
    }
}
