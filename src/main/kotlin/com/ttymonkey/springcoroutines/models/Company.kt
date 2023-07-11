package com.ttymonkey.springcoroutines.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("application.companies")
data class Company(
    @Id val id: Int? = null,
    val name: String,
    val address: String,
)
