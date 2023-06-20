package com.ttymonkey.springcoroutines.repositories

import com.ttymonkey.springcoroutines.models.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Long> {
    fun findByNameContaining(name: String): Flow<User>

    fun findByCompanyId(companyId: Long): Flow<User>

    @Query("SELECT * FROM application.user WHERE email = :email")
    fun findByEmail(email: String): Flow<User>
}
