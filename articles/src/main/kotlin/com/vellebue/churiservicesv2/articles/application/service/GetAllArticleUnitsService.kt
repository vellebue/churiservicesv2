package com.vellebue.churiservicesv2.articles.application.service

import com.vellebue.churiservicesv2.articles.application.ArticleUnitDto
import com.vellebue.churiservicesv2.articles.application.GetAllArticleUnitsUseCase
import com.vellebue.churiservicesv2.articles.domain.ArticleUnitRepository
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetAllArticleUnitsService(
    private val articleUnitRepository: ArticleUnitRepository,
    private val messageSource: MessageSource
) : GetAllArticleUnitsUseCase {

    override fun execute(): List<ArticleUnitDto> {
        return articleUnitRepository.findAll().map {
            ArticleUnitDto(
                symbol = it.symbol,
                description = it.description,
                localizedDescription = messageSource.getMessage(it.translationKey, null, LocaleContextHolder.getLocale())
            )
        }
    }
}
