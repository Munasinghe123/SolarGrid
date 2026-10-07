package com.example.solargrid

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
    private val users = mutableListOf(
        User(
            nic = "111111111V",
            password = "password123",
            role = Role.PROSUMER,
            name = "John Prosumer"
        ),
        User(
            nic = "222222222V",
            password = "password123",
            role = Role.GRID_OPERATOR,
            name = "Jane Operator"
        )
    )

    fun authenticate(nic: String, pass: String): User? {
        return users.find {
            it.nic.equals(nic.trim(), ignoreCase = true) && it.password == pass
        }
    }

    fun registerUser(user: User) {
        users.add(user)
    }
}
