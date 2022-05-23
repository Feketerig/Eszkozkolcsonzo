package utils.browser

import kotlinx.browser.window

object AppState {
    private val reservationstate = "displayPersonalReservationsOnly"

    var displayPersonalReservationsOnly: Boolean
        get() = window.sessionStorage.getItem(reservationstate)?.toBoolean() ?: true
        set(value) = window.sessionStorage.setItem(reservationstate, value.toString())

    fun clear() {
        window.sessionStorage.removeItem(reservationstate)
    }
}