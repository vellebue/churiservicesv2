package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.CreateArticleUseCase
import org.bastanchu.churiservicesv2.articles.application.command.CreateArticleCommand
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnitRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.InvalidArticleException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateArticleService(
    private val articleRepository: ArticleRepository,
    private val articleUnitRepository: ArticleUnitRepository
) : CreateArticleUseCase {

    override fun execute(command: CreateArticleCommand): ArticleDto {
        validateValidityDates(command.beginValidityDate, command.endValidityDate)
        validateFormats(command)

        val article = Article(
            articleId = command.articleId,
            articleName = command.articleName,
            beginValidityDate = command.beginValidityDate,
            endValidityDate = command.endValidityDate,
            formats = command.formats.map { fmt ->
                ArticleFormat(
                    description = fmt.description,
                    referenceUnit = fmt.referenceUnit,
                    eanCode = fmt.eanCode,
                    eanType = fmt.eanType,
                    saleUnit = fmt.saleUnit,
                    conversionFactor = fmt.conversionFactor,
                    articleUnitId = fmt.articleUnitId
                )
            }
        )

        val saved = articleRepository.save(article)
        return saved.toDto()
    }

    private fun validateValidityDates(begin: java.time.LocalDate, end: java.time.LocalDate?) {
        if (end != null && end.isBefore(begin)) {
            throw InvalidArticleException("End validity date must be on or after begin validity date")
        }
    }

    private fun validateFormats(command: CreateArticleCommand) {
        val referenceUnitCount = command.formats.count { it.referenceUnit }
        if (referenceUnitCount == 0) {
            throw InvalidArticleException("At least one format must be configured as reference unit")
        }
        if (referenceUnitCount > 1) {
            throw InvalidArticleException("Only one format can be configured as reference unit")
        }

        command.formats.forEach { fmt ->
            if (articleUnitRepository.findBySymbol(fmt.articleUnitId) == null) {
                throw InvalidArticleException("Article unit '${fmt.articleUnitId}' does not exist")
            }
        }
    }
}
