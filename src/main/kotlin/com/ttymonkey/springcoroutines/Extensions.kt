package com.ttymonkey.springcoroutines

import com.ttymonkey.springcoroutines.dto.*
import com.ttymonkey.springcoroutines.jooq.application.tables.records.CompaniesRecord
import com.ttymonkey.springcoroutines.models.Company
import com.ttymonkey.springcoroutines.models.User

fun UserRequest.toModel(): User =
    User(
        email = this.email,
        name = this.name,
        age = this.age,
        companyId = this.companyId,
    )

fun User.toResponse(): UserResponse =
    UserResponse(
        id = this.id!!,
        email = this.email,
        name = this.name,
        age = this.age,
    )

fun CompanyRequest.toModel(): Company =
    Company(
        name = this.name,
        address = this.address,
    )

fun Company.toResponse(users: List<User> = emptyList()): CompanyResponse =
    CompanyResponse(
        id = this.id!!,
        name = this.name,
        address = this.address,
        users = users.map(User::toResponse),
    )

fun User.toIdNameTypeResponse(): IdNameTypeResponse =
    IdNameTypeResponse(
        id = this.id!!,
        name = this.name,
        type = ResultType.USER,
    )

fun Company.toIdNameTypeResponse(): IdNameTypeResponse =
    IdNameTypeResponse(
        id = this.id!!,
        name = this.name,
        type = ResultType.COMPANY,
    )

fun CompaniesRecord.toCompany(): Company {
    return Company(
        this.get("id", Int::class.java),
        this.get("name", String::class.java),
        this.get("address", String::class.java),
    )
}
