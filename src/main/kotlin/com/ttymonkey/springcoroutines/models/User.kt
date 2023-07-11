package com.ttymonkey.springcoroutines.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("application.users")
data class User(
    @Id val id: Int? = null,
    val email: String,
    val name: String,
    val age: Int,
    val companyId: Int,
)
