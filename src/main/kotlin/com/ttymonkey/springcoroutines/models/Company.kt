package com.ttymonkey.springcoroutines.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("application.company")
data class Company(
    @Id val id: Long? = null,
    val name: String,
    val address: String,
)
