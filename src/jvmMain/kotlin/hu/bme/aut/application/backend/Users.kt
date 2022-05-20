package hu.bme.aut.application.backend

import hu.bme.aut.application.backend.utils.Error
import hu.bme.aut.application.backend.utils.Result
import hu.bme.aut.application.backend.utils.Success
import hu.bme.aut.application.database.Database
import hu.bme.aut.application.database.WrongIdException
import model.User

class Users(private val database: Database) {

    suspend fun getUserByEmail(email: String): Result<User> {
        return try {
            Success(database.getUserByEmail(email))
        } catch (e: WrongIdException) {
            Error(e)
        }
    }

    suspend fun emailAlreadyExists(email: String): Result<Boolean> {
        return Success(database.emailAlreadyExists(email))
    }

    suspend fun addUser(name: String, email: String, phone: String, address: String,
                        pwHash: String, privilege: User.Privilege): Result<Unit> {
        return Success(database.addUser(User(database.getNextUserId(), name, email, phone, address, pwHash, privilege)))
    }

    suspend fun getUserNameById(userId: Int): Result<String> {
        return try {
            Success(database.getUserNameById(userId))
        } catch (e: WrongIdException) {
            Error(e)
        }
    }

    suspend fun getUserById(userId: Int): Result<User> {
        return try {
            Success(database.getUserById(userId))
        } catch (e: WrongIdException) {
            Error(e)
        }
    }
}