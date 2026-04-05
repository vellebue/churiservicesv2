package org.bastanchu.churiservicesv2.articles.domain

interface ArticleRepository {
    fun save(article: Article): Article
    fun findById(id: Long): Article?
    fun deleteById(id: Long)
    fun findByFilter(articleIdPattern: String?, articleNamePattern: String?): List<Article>
}
