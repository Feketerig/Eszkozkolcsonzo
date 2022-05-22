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


    // TODO
    /**
     * Warning! This is not entirely safe, as users can modify session storage
     * However, to get actual info from the server, the entire token has to be sent
     * (This is currently only used for page headline button rendering
     * Which by the way they can do manually anyway, by changing the url, so no effect there either)
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
}