package com.ttymonkey.springcoroutines.controllers

import com.ttymonkey.springcoroutines.dto.IdNameTypeResponse
import com.ttymonkey.springcoroutines.models.Company
import com.ttymonkey.springcoroutines.models.User
import com.ttymonkey.springcoroutines.services.CompanyService
import com.ttymonkey.springcoroutines.services.UserService
import com.ttymonkey.springcoroutines.toIdNameTypeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController(
    private val userService: UserService,
    private val companyService: CompanyService,
) {

    @GetMapping
    suspend fun searchByNames(
        @RequestParam(name = "query") query: String,
    ): Flow<IdNameTypeResponse> {
        val usersFlow = userService.findAllUsersByNameLike(name = query)
            .map(User::toIdNameTypeResponse)
        val companiesFlow = companyService.findAllCompaniesByNameLike(name = query)
            .map(Company::toIdNameTypeResponse)

        return merge(usersFlow, companiesFlow)
    }
}
