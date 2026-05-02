package org.bastanchu.churiservicesv2.articles.infrastructure.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.bastanchu.churiservicesv2.articles.application.PingUseCase
import org.bastanchu.churiservicesv2.common.application.dto.PingStatusDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ping")
@Tag(name = "Ping", description = "Microservice status and dependency health probe")
class PingController(
    private val pingUseCase: PingUseCase
) {

    @GetMapping
    @Operation(
        summary = "Get articles microservice status",
        description = "Public endpoint that reports running status of this microservice. " +
            "When fullMode=true, the response also includes status of dependencies (e.g. PostgreSQL)."
    )
    @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    fun ping(
        @RequestParam(name = "fullMode", required = false, defaultValue = "false") fullMode: Boolean
    ): ResponseEntity<PingStatusDto> {
        return ResponseEntity.ok(pingUseCase.execute(fullMode))
    }
}
