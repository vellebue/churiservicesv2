package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.service.DeleteArticleService
import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.ArticleNotFoundException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class DeleteArticleUseCaseTest {

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @InjectMocks
    private lateinit var useCase: DeleteArticleService

    private val existingArticle = Article(
        id = 1L,
        articleId = "ART001",
        articleName = "Test Article",
        beginValidityDate = LocalDate.of(2026, 1, 1),
        endValidityDate = LocalDate.of(2026, 12, 31),
        formats = listOf(
            ArticleFormat(
                id = 1L, description = "Unit", referenceUnit = true,
                eanCode = null, eanType = null, saleUnit = true,
                conversionFactor = BigDecimal.ONE, articleUnitId = "UN"
            )
        )
    )

    @Test
    fun `should delete existing article`() {
        `when`(articleRepository.findById(1L)).thenReturn(existingArticle)

        useCase.execute(1L)

        verify(articleRepository).deleteById(1L)
    }

    @Test
    fun `should throw when article does not exist`() {
        `when`(articleRepository.findById(99L)).thenReturn(null)

        assertThrows(ArticleNotFoundException::class.java) {
            useCase.execute(99L)
        }
    }
}
