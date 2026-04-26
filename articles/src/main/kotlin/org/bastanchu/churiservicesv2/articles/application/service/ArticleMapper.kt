package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleFormatDto
import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat

fun Article.toDto(): ArticleDto = ArticleDto(
    id = id!!,
    articleId = articleId,
    articleName = articleName,
    beginValidityDate = beginValidityDate,
    endValidityDate = endValidityDate,
    formats = formats.map { it.toDto() }
)

fun ArticleFormat.toDto(): ArticleFormatDto = ArticleFormatDto(
    id = id!!,
    description = description,
    referenceUnit = referenceUnit,
    eanCode = eanCode,
    eanType = eanType,
    saleUnit = saleUnit,
    conversionFactor = conversionFactor,
    articleUnitId = articleUnitId
)
