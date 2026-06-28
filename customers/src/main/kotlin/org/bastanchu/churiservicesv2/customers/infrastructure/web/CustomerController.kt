package org.bastanchu.churiservicesv2.customers.infrastructure.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.infrastructure.logging.LogEntry
import org.bastanchu.churiservicesv2.common.infrastructure.web.pagination.PageQueryParams
import org.bastanchu.churiservicesv2.customers.application.CreateCustomerUseCase
import org.bastanchu.churiservicesv2.customers.application.CreateDelegationUseCase
import org.bastanchu.churiservicesv2.customers.application.DeleteCustomerUseCase
import org.bastanchu.churiservicesv2.customers.application.GetCustomerByIdUseCase
import org.bastanchu.churiservicesv2.customers.application.GetCustomersByFilterUseCase
import org.bastanchu.churiservicesv2.customers.application.UpdateCustomerUseCase
import org.bastanchu.churiservicesv2.customers.application.command.CreateCustomerCommand
import org.bastanchu.churiservicesv2.customers.application.command.CreateDelegationCommand
import org.bastanchu.churiservicesv2.customers.application.command.UpdateCustomerCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerFilterDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Operations for managing customers and their delegations")
@LogEntry
class CustomerController(
    private val createCustomerUseCase: CreateCustomerUseCase,
    private val updateCustomerUseCase: UpdateCustomerUseCase,
    private val deleteCustomerUseCase: DeleteCustomerUseCase,
    private val getCustomerByIdUseCase: GetCustomerByIdUseCase,
    private val getCustomersByFilterUseCase: GetCustomersByFilterUseCase,
    private val createDelegationUseCase: CreateDelegationUseCase
) {

    @PostMapping
    @Operation(summary = "Create a new customer (without delegations)")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    fun create(@Valid @RequestBody command: CreateCustomerCommand): ResponseEntity<CustomerDto> {
        val dto = createCustomerUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(dto)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Full update of an existing customer; delegations are left untouched")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        ApiResponse(responseCode = "404", description = "Customer not found")
    )
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody command: UpdateCustomerCommand
    ): ResponseEntity<CustomerDto> = ResponseEntity.ok(updateCustomerUseCase.execute(id, command))

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer and its addresses and delegations")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        ApiResponse(responseCode = "404", description = "Customer not found")
    )
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        deleteCustomerUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by id including its addresses and delegations")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Customer found"),
        ApiResponse(responseCode = "404", description = "Customer not found")
    )
    fun getById(@PathVariable id: Long): ResponseEntity<CustomerDto> =
        ResponseEntity.ok(getCustomerByIdUseCase.execute(id))

    @GetMapping
    @Operation(
        summary = "Query customers by filter (paged)",
        description = "Use '*' as wildcard on commercialName, socialName, vatNumber"
    )
    @ApiResponse(responseCode = "200", description = "Paged customers matching the filter")
    fun getByFilter(
        @RequestParam(required = false) commercialName: String?,
        @RequestParam(required = false) socialName: String?,
        @RequestParam(required = false) vatNumber: String?,
        @Valid @ModelAttribute pageParams: PageQueryParams
    ): ResponseEntity<FilteredPage<CustomerDto>> {
        val filter = CustomerFilterDto(
            commercialName = commercialName,
            socialName = socialName,
            vatNumber = vatNumber
        )
        return ResponseEntity.ok(getCustomersByFilterUseCase.execute(filter, pageParams.toPageRequest()))
    }

    @PostMapping("/{customerId}/delegations")
    @Operation(summary = "Add a delegation to an existing customer")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Delegation created successfully"),
        ApiResponse(responseCode = "404", description = "Customer not found")
    )
    fun createDelegation(
        @PathVariable customerId: Long,
        @Valid @RequestBody command: CreateDelegationCommand
    ): ResponseEntity<CustomerDelegationDto> {
        val dto = createDelegationUseCase.execute(customerId, command)
        return ResponseEntity.status(HttpStatus.CREATED).body(dto)
    }
}
