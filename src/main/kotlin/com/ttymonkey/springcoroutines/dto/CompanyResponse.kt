package com.ttymonkey.springcoroutines.dto

data class CompanyResponse(
    val id: Long,
    val name: String,
    val address: String,
    val users: List<UserResponse>,
)
