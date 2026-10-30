package com.example.solargrid.auth

enum class Role {
    PROSUMER,
    GRID_OPERATOR
}

data class User(
    val nic: String,
    val password: String,
    val role: Role,
    val name: String
)

object UserRepository {
    private val users = mutableListOf<User>()

    fun authenticate(nic: String, pass: String): User? {
        return users.find {
            it.nic.equals(nic.trim(), ignoreCase = true) && it.password == pass
        }
    }

    fun registerUser(user: User) {
        users.add(user)
    }
}
