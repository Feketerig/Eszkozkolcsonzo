package hu.bme.aut.application.backend

import hu.bme.aut.application.backend.utils.*
import hu.bme.aut.application.database.Database
import hu.bme.aut.application.database.WrongIdException
import hu.bme.aut.application.security.JwtConfig
import model.User

class Users(private val database: Database) {

    suspend fun getUserByEmail(email: String): Result<User> {
        return try {
            Success(database.getUserByEmail(email))
        } catch (e: WrongIdException) {
            NotFound(-1)
        }
    }

    suspend fun registerUser(name: String, email: String, phone: String, address: String,
                             pwHash: String, privilege: User.Privilege): Result<Unit> {
        return if (database.emailAlreadyExists(email).not()) {
            Success(database.addUser(User(database.getNextUserId(), name, email, phone, address, pwHash, privilege)))
        } else {
            Conflict("email")
        }
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