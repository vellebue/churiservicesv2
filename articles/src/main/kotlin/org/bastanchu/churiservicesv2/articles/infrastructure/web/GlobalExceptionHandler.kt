package org.bastanchu.churiservicesv2.articles.infrastructure.web

import org.bastanchu.churiservicesv2.articles.domain.exception.ArticleNotFoundException
import org.bastanchu.churiservicesv2.articles.domain.exception.InvalidArticleException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ArticleNotFoundException::class)
    fun handleArticleNotFound(ex: ArticleNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(error = "Not Found", message = ex.message ?: "Article not found"))
    }

    @ExceptionHandler(InvalidArticleException::class)
    fun handleInvalidArticle(ex: InvalidArticleException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(error = "Bad Request", message = ex.message ?: "Invalid article"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val messages = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(error = "Validation Error", message = messages.joinToString("; ")))
    }
}

data class ErrorResponse(val error: String, val message: String)
