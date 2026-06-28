package org.bastanchu.churiservicesv2.common.infrastructure.web.pagination

import org.bastanchu.churiservicesv2.common.domain.exception.InvalidPageRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class PaginationExceptionHandler {

    @ExceptionHandler(InvalidPageRequestException::class)
    fun handleInvalidPageRequest(ex: InvalidPageRequestException): ProblemDetail {
        val detail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.message ?: "Invalid page request",
        )
        detail.title = "Invalid page request"
        return detail
    }
}
