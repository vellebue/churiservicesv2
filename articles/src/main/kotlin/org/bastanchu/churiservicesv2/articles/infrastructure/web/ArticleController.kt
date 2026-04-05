package org.bastanchu.churiservicesv2.articles.infrastructure.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.bastanchu.churiservicesv2.articles.application.CreateArticleUseCase
import org.bastanchu.churiservicesv2.articles.application.DeleteArticleUseCase
import org.bastanchu.churiservicesv2.articles.application.GetArticleByIdUseCase
import org.bastanchu.churiservicesv2.articles.application.GetArticlesByFilterUseCase
import org.bastanchu.churiservicesv2.articles.application.UpdateArticleUseCase
import org.bastanchu.churiservicesv2.articles.application.command.CreateArticleCommand
import org.bastanchu.churiservicesv2.articles.application.command.UpdateArticleCommand
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleFilterDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/articles")
@Tag(name = "Articles", description = "Operations for managing articles and their formats")
class ArticleController(
    private val createArticleUseCase: CreateArticleUseCase,
    private val updateArticleUseCase: UpdateArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    private val getArticleByIdUseCase: GetArticleByIdUseCase,
    private val getArticlesByFilterUseCase: GetArticlesByFilterUseCase
) {

    @PostMapping
    @Operation(summary = "Create a new article with formats")
    @ApiResponse(responseCode = "201", description = "Article created successfully")
    fun create(@Valid @RequestBody command: CreateArticleCommand): ResponseEntity<ArticleDto> {
        val dto = createArticleUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(dto)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing article with formats")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Article updated successfully"),
        ApiResponse(responseCode = "404", description = "Article not found")
    )
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody command: UpdateArticleCommand
    ): ResponseEntity<ArticleDto> {
        return ResponseEntity.ok(updateArticleUseCase.execute(id, command))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an article by numeric id")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Article deleted successfully"),
        ApiResponse(responseCode = "404", description = "Article not found")
    )
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        deleteArticleUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an article by numeric id")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Article found"),
        ApiResponse(responseCode = "404", description = "Article not found")
    )
    fun getById(@PathVariable id: Long): ResponseEntity<ArticleDto> {
        return ResponseEntity.ok(getArticleByIdUseCase.execute(id))
    }

    @GetMapping
    @Operation(summary = "Query articles by filter", description = "Use '*' as wildcard in articleId and articleName")
    @ApiResponse(responseCode = "200", description = "List of matching articles")
    fun getByFilter(
        @RequestParam(required = false) articleId: String?,
        @RequestParam(required = false) articleName: String?
    ): ResponseEntity<List<ArticleDto>> {
        val filter = ArticleFilterDto(articleId = articleId, articleName = articleName)
        return ResponseEntity.ok(getArticlesByFilterUseCase.execute(filter))
    }
}
