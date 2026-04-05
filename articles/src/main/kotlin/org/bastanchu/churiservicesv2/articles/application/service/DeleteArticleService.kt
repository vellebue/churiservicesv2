package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.DeleteArticleUseCase
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.ArticleNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteArticleService(
    private val articleRepository: ArticleRepository
) : DeleteArticleUseCase {

    override fun execute(id: Long) {
        articleRepository.findById(id)
            ?: throw ArticleNotFoundException(id)
        articleRepository.deleteById(id)
    }
}
