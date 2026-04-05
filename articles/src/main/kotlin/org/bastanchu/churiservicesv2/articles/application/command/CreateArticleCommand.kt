package org.bastanchu.churiservicesv2.articles.application.command

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class CreateArticleCommand(
    @field:NotBlank
    @field:Size(max = 10)
    val articleId: String,

    @field:NotBlank
    @field:Size(max = 150)
    val articleName: String,

    @field:NotEmpty
    @field:Valid
    val formats: List<ArticleFormatCommand>
)
