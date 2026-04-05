package org.bastanchu.churiservicesv2.articles.domain.exception

class ArticleNotFoundException(val articleId: Long) :
    RuntimeException("Article with id $articleId not found")
