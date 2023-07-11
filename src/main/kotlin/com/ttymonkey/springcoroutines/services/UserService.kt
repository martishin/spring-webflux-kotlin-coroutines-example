package com.ttymonkey.springcoroutines.services

import com.ttymonkey.springcoroutines.models.User
import com.ttymonkey.springcoroutines.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun saveUser(user: User): User? =
        userRepository.findByEmail(user.email)
            .firstOrNull()
            ?.let { throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The spcified emnail is already in use.") }
            ?: userRepository.save(user)

    fun findAllUsers(): Flow<User> =
        userRepository.findAll()

    suspend fun findUserById(id: Int): User? =
        userRepository.findById(id)

    suspend fun deleteUserById(id: Int) {
        val foundUser = userRepository.findById(id)

        if (foundUser == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $id was not found.")
        } else {
            userRepository.deleteById(id)
        }
    }

    suspend fun updateUser(id: Int, requestedUser: User): User {
        val foundUser = userRepository.findById(id)

        return if (foundUser == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $id was not found.")
        } else {
            userRepository.save(requestedUser.copy(id = foundUser.id))
        }
    }

    fun findAllUsersByNameLike(name: String): Flow<User> =
        userRepository.findByNameContaining(name)

    fun findUsersByCompanyId(id: Int): Flow<User> =
        userRepository.findByCompanyId(id)
}
