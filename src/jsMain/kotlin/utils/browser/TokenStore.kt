package utils.browser

import kotlinx.browser.window
import model.User
import utils.hash.base64Decode

object TokenStore {
    private const val jwt_token = "auth_token"

    fun setJwtToken(value: String) {
        window.sessionStorage.setItem(jwt_token, value)
    }

    fun getJwtToken(): String {
        return window.sessionStorage.getItem(jwt_token) ?: ""
    }

    fun clear() {
        window.sessionStorage.removeItem(jwt_token)
    }

    /**
     * Warning! This is not entirely safe, as users can modify session storage
     * This should only be used for rendering certain buttons and ui elements, NOT ACTUAL AUTHENTICATION
     * To use the functionality, the entire authentication token has to be sent with the requests,
     * which will be validated by the server anyway.
     */
    fun getUserPrivilege(): User.Privilege? {
        return try {
            val token = window.sessionStorage.getItem(jwt_token)!!.base64Decode()
            val priv = token.split("priv\":\"")[1].takeWhile { c -> c != '\"' }
            User.Privilege.valueOf(priv)
        } catch (e: Exception) {
            null
        }
    }

    fun getUserId(): Int? {
        return try {
            val token = window.sessionStorage.getItem(jwt_token)!!.base64Decode()
            val id = token.split("\"id\":")[1].takeWhile { c -> c.isDigit() }
            id.toInt()
        } catch (e: Exception) {
            null
        }
    }
}