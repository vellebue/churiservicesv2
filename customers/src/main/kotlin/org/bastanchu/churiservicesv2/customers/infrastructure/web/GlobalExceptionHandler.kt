package org.bastanchu.churiservicesv2.customers.infrastructure.web

import org.bastanchu.churiservicesv2.customers.domain.exception.CustomerNotFoundException
import org.bastanchu.churiservicesv2.customers.domain.exception.DelegationNotFoundException
import org.bastanchu.churiservicesv2.customers.domain.exception.InvalidCustomerException
import org.bastanchu.churiservicesv2.customers.domain.exception.InvalidDelegationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleCustomerNotFound(ex: CustomerNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(error = "Not Found", message = ex.message ?: "Customer not found"))

    @ExceptionHandler(DelegationNotFoundException::class)
    fun handleDelegationNotFound(ex: DelegationNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(error = "Not Found", message = ex.message ?: "Delegation not found"))

    @ExceptionHandler(InvalidCustomerException::class)
    fun handleInvalidCustomer(ex: InvalidCustomerException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(error = "Bad Request", message = ex.message ?: "Invalid customer"))

    @ExceptionHandler(InvalidDelegationException::class)
    fun handleInvalidDelegation(ex: InvalidDelegationException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(error = "Bad Request", message = ex.message ?: "Invalid delegation"))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val messages = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(error = "Validation Error", message = messages.joinToString("; ")))
    }
}

data class ErrorResponse(val error: String, val message: String)
