package utils.browser

import kotlinx.browser.window

object TokenStore {
    private const val jwt_token = "auth_token"

    fun put(value: String, key: String = jwt_token) {
        window.sessionStorage.setItem(key, value)
    }

    fun get(key: String = jwt_token): String {
        return window.sessionStorage.getItem(key) ?: ""
    }
}