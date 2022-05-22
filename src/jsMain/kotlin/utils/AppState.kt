package utils

import kotlinx.browser.window

object AppState {
    var reservations_onlyown: Boolean
        get() = window.sessionStorage.getItem("reservations_onlyown")?.toBoolean() ?: true
        set(value) = window.sessionStorage.setItem("reservations_onlyown", value.toString())

    fun clear() {
        window.sessionStorage.clear()
    }
}