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

    fun get(key: String = jwt_token): String {
        return window.sessionStorage.getItem(key) ?: ""
    }
}