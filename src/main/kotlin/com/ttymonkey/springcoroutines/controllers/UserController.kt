package com.ttymonkey.springcoroutines.controllers

import com.ttymonkey.springcoroutines.dto.UserRequest
import com.ttymonkey.springcoroutines.dto.UserResponse
import com.ttymonkey.springcoroutines.models.User
import com.ttymonkey.springcoroutines.services.UserService
import com.ttymonkey.springcoroutines.toModel
import com.ttymonkey.springcoroutines.toResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @PostMapping
    suspend fun createUser(@RequestBody userRequest: UserRequest): UserResponse =
        userService.saveUser(
            user = userRequest.toModel(),
        )
            ?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error during user creation.")

    @GetMapping
    fun findUsers(
        @RequestParam("name", required = false) name: String?,
    ): Flow<UserResponse> {
        val users = name?.let { userService.findAllUsersByNameLike(it) }
            ?: userService.findAllUsers()

        return users.map(User::toResponse)
    }

    @GetMapping("/{id}")
    suspend fun findUserById(
        @PathVariable id: Long,
    ): UserResponse =
        userService.findUserById(id)
            ?.let(User::toResponse)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $id was not found.")

    @DeleteMapping("/{id}")
    suspend fun deleteUserById(
        @PathVariable id: Long,
    ) {
        userService.deleteUserById(id)
    }

    @PutMapping("/{id}")
    suspend fun updateUser(
        @PathVariable id: Long,
        @RequestBody userRequest: UserRequest,
    ): UserResponse =
        userService.updateUser(
            id = id,
            requestedUser = userRequest.toModel(),
        ).toResponse()
}
