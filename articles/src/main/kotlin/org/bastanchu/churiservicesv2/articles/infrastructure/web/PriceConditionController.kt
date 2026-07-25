package org.bastanchu.churiservicesv2.articles.infrastructure.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.bastanchu.churiservicesv2.articles.application.CreatePriceConditionUseCase
import org.bastanchu.churiservicesv2.articles.application.DeletePriceConditionUseCase
import org.bastanchu.churiservicesv2.articles.application.GetPriceConditionByIdUseCase
import org.bastanchu.churiservicesv2.articles.application.GetPriceConditionsByFilterUseCase
import org.bastanchu.churiservicesv2.articles.application.UpdatePriceConditionUseCase
import org.bastanchu.churiservicesv2.articles.application.command.CreatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.command.UpdatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionFilterDto
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.infrastructure.logging.LogEntry
import org.bastanchu.churiservicesv2.common.infrastructure.web.pagination.PageQueryParams
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
@RequestMapping("/api/price-conditions")
@Tag(name = "Price Conditions", description = "Operations for managing price conditions and their values")
@LogEntry
class PriceConditionController(
    private val createPriceConditionUseCase: CreatePriceConditionUseCase,
    private val updatePriceConditionUseCase: UpdatePriceConditionUseCase,
    private val deletePriceConditionUseCase: DeletePriceConditionUseCase,
    private val getPriceConditionByIdUseCase: GetPriceConditionByIdUseCase,
    private val getPriceConditionsByFilterUseCase: GetPriceConditionsByFilterUseCase
) {

    @PostMapping
    @Operation(summary = "Create a new price condition with values")
    @ApiResponse(responseCode = "201", description = "Price condition created successfully")
    fun create(@Valid @RequestBody command: CreatePriceConditionCommand): ResponseEntity<PriceConditionDto> {
        val dto = createPriceConditionUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(dto)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing price condition with values")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Price condition updated successfully"),
        ApiResponse(responseCode = "404", description = "Price condition not found")
    )
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody command: UpdatePriceConditionCommand
    ): ResponseEntity<PriceConditionDto> {
        return ResponseEntity.ok(updatePriceConditionUseCase.execute(id, command))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a price condition by id")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Price condition deleted successfully"),
        ApiResponse(responseCode = "404", description = "Price condition not found")
    )
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        deletePriceConditionUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a price condition by id including its values")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Price condition found"),
        ApiResponse(responseCode = "404", description = "Price condition not found")
    )
    fun getById(@PathVariable id: Long): ResponseEntity<PriceConditionDto> {
        return ResponseEntity.ok(getPriceConditionByIdUseCase.execute(id))
    }

    @GetMapping
    @Operation(
        summary = "Query price conditions by filter",
        description = "Use '*' as wildcard in description. Results are paginated."
    )
    @ApiResponse(responseCode = "200", description = "Paged list of matching price conditions")
    fun getByFilter(
        @RequestParam(required = false) conditionType: String?,
        @RequestParam(required = false) description: String?,
        @Valid pageQueryParams: PageQueryParams
    ): ResponseEntity<FilteredPage<PriceConditionDto>> {
        val filter = PriceConditionFilterDto(conditionType = conditionType, description = description)
        val result = getPriceConditionsByFilterUseCase.execute(filter, pageQueryParams.toPageRequest())
        return ResponseEntity.ok(result)
    }
}
