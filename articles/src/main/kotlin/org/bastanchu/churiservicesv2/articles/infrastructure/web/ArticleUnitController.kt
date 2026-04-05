package org.bastanchu.churiservicesv2.articles.infrastructure.web

import org.bastanchu.churiservicesv2.articles.application.ArticleUnitDto
import org.bastanchu.churiservicesv2.articles.application.GetAllArticleUnitsUseCase
import org.bastanchu.churiservicesv2.articles.application.GetArticleUnitBySymbolUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/article-units")
@Tag(name = "Article Units", description = "Operations for managing article measure units")
class ArticleUnitController(
    private val getAllArticleUnitsUseCase: GetAllArticleUnitsUseCase,
    private val getArticleUnitBySymbolUseCase: GetArticleUnitBySymbolUseCase
) {

    @GetMapping
    @Operation(summary = "Get all article units", description = "Returns a list of all article measure units")
    @ApiResponse(responseCode = "200", description = "List of article units retrieved successfully")
    fun getAllArticleUnits(): ResponseEntity<List<ArticleUnitDto>> {
        return ResponseEntity.ok(getAllArticleUnitsUseCase.execute())
    }

    @GetMapping("/article-unit/{symbol}")
    @Operation(summary = "Get article unit by symbol", description = "Returns a single article unit by its symbol")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Article unit found"),
        ApiResponse(responseCode = "404", description = "Article unit not found")
    )
    fun getArticleUnitBySymbol(@PathVariable symbol: String): ResponseEntity<ArticleUnitDto> {
        val articleUnit = getArticleUnitBySymbolUseCase.execute(symbol)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(articleUnit)
    }
}
