package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.UpdateArticleUseCase
import org.bastanchu.churiservicesv2.articles.application.command.UpdateArticleCommand
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnitRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.ArticleNotFoundException
import org.bastanchu.churiservicesv2.articles.domain.exception.InvalidArticleException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateArticleService(
    private val articleRepository: ArticleRepository,
    private val articleUnitRepository: ArticleUnitRepository
) : UpdateArticleUseCase {

    override fun execute(id: Long, command: UpdateArticleCommand): ArticleDto {
        articleRepository.findById(id)
            ?: throw ArticleNotFoundException(id)

        validateFormats(command)

        val updatedArticle = Article(
            id = id,
            articleId = command.articleId,
            articleName = command.articleName,
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

        val saved = articleRepository.save(updatedArticle)
        return saved.toDto()
    }

    private fun validateFormats(command: UpdateArticleCommand) {
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
