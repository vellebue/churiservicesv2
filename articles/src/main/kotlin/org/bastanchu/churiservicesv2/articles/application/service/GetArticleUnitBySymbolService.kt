package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.dto.ArticleUnitDto
import org.bastanchu.churiservicesv2.articles.application.GetArticleUnitBySymbolUseCase
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnitRepository
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetArticleUnitBySymbolService(
    private val articleUnitRepository: ArticleUnitRepository,
    private val messageSource: MessageSource
) : GetArticleUnitBySymbolUseCase {

    override fun execute(symbol: String): ArticleUnitDto? {
        return articleUnitRepository.findBySymbol(symbol)?.let {
            ArticleUnitDto(
                symbol = it.symbol,
                description = it.description,
                localizedDescription = messageSource.getMessage(it.translationKey, null, LocaleContextHolder.getLocale())
            )
        }
    }
}
