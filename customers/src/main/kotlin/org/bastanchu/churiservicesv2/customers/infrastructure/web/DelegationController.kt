package org.bastanchu.churiservicesv2.customers.infrastructure.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.infrastructure.logging.LogEntry
import org.bastanchu.churiservicesv2.common.infrastructure.web.pagination.PageQueryParams
import org.bastanchu.churiservicesv2.customers.application.DeleteDelegationUseCase
import org.bastanchu.churiservicesv2.customers.application.GetDelegationByIdUseCase
import org.bastanchu.churiservicesv2.customers.application.GetDelegationsByFilterUseCase
import org.bastanchu.churiservicesv2.customers.application.UpdateDelegationUseCase
import org.bastanchu.churiservicesv2.customers.application.command.UpdateDelegationCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto
import org.bastanchu.churiservicesv2.customers.application.dto.DelegationFilterDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/delegations")
@Tag(name = "Delegations", description = "Operations for managing customer delegations")
@LogEntry
class DelegationController(
    private val updateDelegationUseCase: UpdateDelegationUseCase,
    private val deleteDelegationUseCase: DeleteDelegationUseCase,
    private val getDelegationByIdUseCase: GetDelegationByIdUseCase,
    private val getDelegationsByFilterUseCase: GetDelegationsByFilterUseCase
) {

    @PutMapping("/{id}")
    @Operation(summary = "Full update of an existing delegation")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Delegation updated successfully"),
        ApiResponse(responseCode = "404", description = "Delegation not found")
    )
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody command: UpdateDelegationCommand
    ): ResponseEntity<CustomerDelegationDto> =
        ResponseEntity.ok(updateDelegationUseCase.execute(id, command))

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a delegation by id")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Delegation deleted successfully"),
        ApiResponse(responseCode = "404", description = "Delegation not found")
    )
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        deleteDelegationUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a delegation by id with full detail")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Delegation found"),
        ApiResponse(responseCode = "404", description = "Delegation not found")
    )
    fun getById(@PathVariable id: Long): ResponseEntity<CustomerDelegationDto> =
        ResponseEntity.ok(getDelegationByIdUseCase.execute(id))

    @GetMapping
    @Operation(
        summary = "Query delegations by filter (paged)",
        description = "Use '*' as wildcard on name"
    )
    @ApiResponse(responseCode = "200", description = "Paged delegations matching the filter")
    fun getByFilter(
        @RequestParam(required = false) name: String?,
        @Valid @ModelAttribute pageParams: PageQueryParams
    ): ResponseEntity<FilteredPage<CustomerDelegationDto>> {
        val filter = DelegationFilterDto(name = name)
        return ResponseEntity.ok(getDelegationsByFilterUseCase.execute(filter, pageParams.toPageRequest()))
    }
}
