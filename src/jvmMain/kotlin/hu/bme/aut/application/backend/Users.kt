package hu.bme.aut.application.backend

import hu.bme.aut.application.backend.utils.*
import hu.bme.aut.application.database.Database
import hu.bme.aut.application.database.WrongIdException
import hu.bme.aut.application.security.JwtConfig
import model.User
import utils.validators.isValidEmail
import utils.validators.isValidNameHU
import utils.validators.isValidPhoneNumber

class Users(private val database: Database) {

    suspend fun getUserByEmail(email: String): Result<User> {
        return try {
            Success(database.getUserByEmail(email))
        } catch (e: WrongIdException) {
            NotFound(-1)
        }
    }

    suspend fun registerUser(name: String, email: String, phone: String, address: String, pwHash: String): Result<Unit> {
        if (database.emailAlreadyExists(email)) return Conflict("Email already exist")

        if (!name.isValidNameHU()) return PreconditionFailed("name format")
        if (!phone.isValidPhoneNumber()) return PreconditionFailed("phone number format")
        if (!email.isValidEmail()) return PreconditionFailed("email format")

        return Success(database.addUser(User(database.getNextUserId(), name, email, phone, address, pwHash, User.Privilege.User)))
    }

    suspend fun getUserNameById(userId: Int): Result<String> {
        return try {
            Success(database.getUserNameById(userId))
        } catch (e: WrongIdException) {
            NotFound(userId)
        }
    }

    suspend fun getUserById(userId: Int): Result<User> {
        return try {
            Success(database.getUserById(userId))
        } catch (e: WrongIdException) {
            NotFound(userId)
        }
    }

    suspend fun loginWith(email: String, pwHash: String): Result<String> {
        return try {
            val user = database.getUserByEmail(email)
            if (user.password_hash == pwHash) {
                val token = JwtConfig.createAccessToken(user.id, user.name, user.email, user.privilege.toString())
                Success(token)
            } else {
                Unauthorized()
            }
        } catch (e: WrongIdException) {
            NotFound(-1)
        }
    }
}