package com.ttymonkey.springcoroutines.controllers

import com.ttymonkey.springcoroutines.dto.CompanyRequest
import com.ttymonkey.springcoroutines.dto.CompanyResponse
import com.ttymonkey.springcoroutines.models.Company
import com.ttymonkey.springcoroutines.services.CompanyService
import com.ttymonkey.springcoroutines.services.UserService
import com.ttymonkey.springcoroutines.toModel
import com.ttymonkey.springcoroutines.toResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/companies")
class CompanyController(
    private val companyService: CompanyService,
    private val userService: UserService,
) {
    companion object {
        private val log = LoggerFactory.getLogger(CompanyService::class.java)
    }

    @PostMapping
    suspend fun createCompany(@RequestBody companyRequest: CompanyRequest): CompanyResponse =
        companyService.saveCompany(
            company = companyRequest.toModel(),
        )
            ?.toResponse()
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error during company creation.",
            )

    @GetMapping
    fun findCompany(
        @RequestParam("name", required = false) name: String?,
    ): Flow<CompanyResponse> {
        val companies = name?.let { companyService.findAllCompaniesByNameLike(name) }
            ?: companyService.findAllCompanies()

        return companies
            .map { company ->
                company.toResponse(
                    users = findCompanyUsers(company),
                )
            }
    }

    @GetMapping("/{id}")
    suspend fun findCompanyById(
        @PathVariable id: Int,
    ): CompanyResponse =
        companyService.findCompanyById(id)
            ?.let { company ->
                company.toResponse(
                    users = findCompanyUsers(company),
                )
            }
            ?: run {
                log.info("Company with id $id was not found.")
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Company with id $id was not found.")
            }

    @DeleteMapping("/{id}")
    suspend fun deleteCompanyById(
        @PathVariable id: Int,
    ): ResponseEntity<Void> {
        companyService.deleteCompanyById(id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}")
    suspend fun updateCompany(
        @PathVariable id: Int,
        @RequestBody companyRequest: CompanyRequest,
    ): CompanyResponse =
        companyService.updateCompany(
            id = id,
            requestedCompany = companyRequest.toModel(),
        )
            .let { company ->
                company.toResponse(
                    users = findCompanyUsers(company),
                )
            }

    private suspend fun findCompanyUsers(company: Company) =
        userService.findUsersByCompanyId(company.id!!)
            .toList()
}
