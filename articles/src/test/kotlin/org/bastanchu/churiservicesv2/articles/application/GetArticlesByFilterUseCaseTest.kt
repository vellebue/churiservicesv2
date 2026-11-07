package org.bastanchu.churiservicesv2.articles.application

import jdk.jfr.Description
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleFilterDto
import org.bastanchu.churiservicesv2.articles.application.service.GetArticlesByFilterService
import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.common.domain.exception.InvalidPageRequestException
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.domain.pagination.SortCriterion
import org.bastanchu.churiservicesv2.common.domain.pagination.SortDirection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
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

    private fun pageOf(items: List<Article>, sort: List<SortCriterion> = emptyList()): FilteredPage<Article> =
        FilteredPage(
            content = items,
            offset = 0L,
            limit = 20,
            hasMore = false,
            sort = sort
        )

    @Test
    fun `should return articles matching filter`() {
        val filter = ArticleFilterDto(articleId = "ART*", articleName = null)
        whenever(articleRepository.findByFilter(eq("ART*"), eq(null), any(), any()))
            .thenReturn(pageOf(articles))

        val result = useCase.execute(filter, PageRequest.defaults())

        assertEquals(2, result.content.size)
        assertEquals("ART001", result.content[0].articleId)
        assertEquals("ART002", result.content[1].articleId)
    }

    @Test
    fun `should return empty page when no match`() {
        val filter = ArticleFilterDto(articleId = "ZZZ*", articleName = null)
        whenever(articleRepository.findByFilter(eq("ZZZ*"), eq(null), any(), any()))
            .thenReturn(pageOf(emptyList()))

        val result = useCase.execute(filter, PageRequest.defaults())

        assertTrue(result.content.isEmpty())
    }

    @Test
    fun `should return all articles when filter is empty`() {
        val filter = ArticleFilterDto()
        whenever(articleRepository.findByFilter(eq(null), eq(null), any(), any()))
            .thenReturn(pageOf(articles))

        val result = useCase.execute(filter, PageRequest.defaults())

        assertEquals(2, result.content.size)
    }

    @Test
    @Description("The purpose of this test is to verify that the current date is passed as reference for validity filtering. Remember that those articles whose validity ends before the current date are not returned under any circumstance.")
    fun `should pass current date as reference for validity filtering`() {
        val filter = ArticleFilterDto()
        val dateCaptor = argumentCaptor<LocalDate>()
        whenever(articleRepository.findByFilter(eq(null), eq(null), dateCaptor.capture(), any()))
            .thenReturn(pageOf(emptyList()))

        useCase.execute(filter, PageRequest.defaults())

        assertEquals(LocalDate.now(), dateCaptor.firstValue)
    }

    @Test
    fun `should append articleId tie-breaker to sort`() {
        val pageCaptor = argumentCaptor<PageRequest>()
        whenever(articleRepository.findByFilter(eq(null), eq(null), any(), pageCaptor.capture()))
            .thenReturn(pageOf(emptyList()))
        val requested = PageRequest(
            offset = 0L,
            limit = 20,
            sort = listOf(SortCriterion("articleName", SortDirection.DESC))
        )

        useCase.execute(ArticleFilterDto(), requested)

        val resolvedSort = pageCaptor.firstValue.sort
        assertTrue(resolvedSort.last().field == "articleId" && resolvedSort.last().direction == SortDirection.ASC)
    }

    @Test
    fun `should reject sort on non-whitelisted field`() {
        val requested = PageRequest(
            offset = 0L,
            limit = 20,
            sort = listOf(SortCriterion("beginValidityDate", SortDirection.ASC))
        )

        assertThrows(InvalidPageRequestException::class.java) {
            useCase.execute(ArticleFilterDto(), requested)
        }
    }
}
