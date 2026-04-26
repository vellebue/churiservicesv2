package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.ArticleFormatCommand
import org.bastanchu.churiservicesv2.articles.application.command.CreateArticleCommand
import org.bastanchu.churiservicesv2.articles.application.service.CreateArticleService
import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnit
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnitRepository
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
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class CreateArticleUseCaseTest {

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @Mock
    private lateinit var articleUnitRepository: ArticleUnitRepository

    @InjectMocks
    private lateinit var useCase: CreateArticleService

    private val beginValidity = LocalDate.of(2026, 1, 1)
    private val endValidity = LocalDate.of(2026, 12, 31)

    private val validFormatCommand = ArticleFormatCommand(
        description = "Unit",
        referenceUnit = true,
        eanCode = "1234567890123",
        eanType = "EAN13",
        saleUnit = true,
        conversionFactor = BigDecimal.ONE,
        articleUnitId = "UN"
    )

    private val validCommand = CreateArticleCommand(
        articleId = "ART001",
        articleName = "Test Article",
        beginValidityDate = beginValidity,
        endValidityDate = endValidity,
        formats = listOf(validFormatCommand)
    )

    @Test
    fun `should create article with valid formats`() {
        val savedArticle = Article(
            id = 1L,
            articleId = "ART001",
            articleName = "Test Article",
            beginValidityDate = beginValidity,
            endValidityDate = endValidity,
            formats = listOf(
                ArticleFormat(
                    id = 1L,
                    description = "Unit",
                    referenceUnit = true,
                    eanCode = "1234567890123",
                    eanType = "EAN13",
                    saleUnit = true,
                    conversionFactor = BigDecimal.ONE,
                    articleUnitId = "UN"
                )
            )
        )

        `when`(articleUnitRepository.findBySymbol("UN"))
            .thenReturn(ArticleUnit(symbol = "UN", description = "Unit", translationKey = "article.unit.un"))
        whenever(articleRepository.save(any())).thenReturn(savedArticle)

        val result = useCase.execute(validCommand)

        assertEquals(1L, result.id)
        assertEquals("ART001", result.articleId)
        assertEquals("Test Article", result.articleName)
        assertEquals(beginValidity, result.beginValidityDate)
        assertEquals(endValidity, result.endValidityDate)
        assertEquals(1, result.formats.size)
        assertEquals(true, result.formats[0].referenceUnit)
    }

    @Test
    fun `should create article without end validity date`() {
        val savedArticle = Article(
            id = 1L,
            articleId = "ART001",
            articleName = "Test Article",
            beginValidityDate = beginValidity,
            endValidityDate = null,
            formats = listOf(
                ArticleFormat(
                    id = 1L,
                    description = "Unit",
                    referenceUnit = true,
                    eanCode = "1234567890123",
                    eanType = "EAN13",
                    saleUnit = true,
                    conversionFactor = BigDecimal.ONE,
                    articleUnitId = "UN"
                )
            )
        )

        `when`(articleUnitRepository.findBySymbol("UN"))
            .thenReturn(ArticleUnit(symbol = "UN", description = "Unit", translationKey = "article.unit.un"))
        whenever(articleRepository.save(any())).thenReturn(savedArticle)

        val result = useCase.execute(validCommand.copy(endValidityDate = null))

        assertEquals(beginValidity, result.beginValidityDate)
        assertEquals(null, result.endValidityDate)
    }

    @Test
    fun `should throw when end validity date is before begin validity date`() {
        val command = validCommand.copy(
            beginValidityDate = LocalDate.of(2026, 6, 1),
            endValidityDate = LocalDate.of(2026, 1, 1)
        )

        assertThrows(InvalidArticleException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw when no format is configured as reference unit`() {
        val command = validCommand.copy(
            formats = listOf(validFormatCommand.copy(referenceUnit = false))
        )

        assertThrows(InvalidArticleException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw when multiple formats are configured as reference unit`() {
        val command = validCommand.copy(
            formats = listOf(
                validFormatCommand.copy(referenceUnit = true),
                validFormatCommand.copy(description = "Box", referenceUnit = true)
            )
        )

        assertThrows(InvalidArticleException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw when article unit does not exist`() {
        val command = validCommand.copy(
            formats = listOf(validFormatCommand.copy(articleUnitId = "INVALID"))
        )

        `when`(articleUnitRepository.findBySymbol("INVALID")).thenReturn(null)

        assertThrows(InvalidArticleException::class.java) {
            useCase.execute(command)
        }
    }
}
