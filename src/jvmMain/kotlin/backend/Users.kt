package backend

import database.Database
import model.User

class Users(private val database: Database) {

    suspend fun getUserByEmail(email: String): Result<User> {
        TODO()
    }

    suspend fun emailAlreadyExists(email: String): Result<Boolean> {
        TODO()
    }

    suspend fun addUser(user: User): Result<Unit> {
        TODO()
    }

    suspend fun getUserNameById(userId: Int): Result<String> {
        TODO()
    }

    suspend fun getUserById(userId: Int): Result<User> {
        TODO()
    }
}