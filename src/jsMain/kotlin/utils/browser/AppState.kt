package utils.browser

import kotlinx.browser.window

object AppState {
    private const val reservationState = "displayPersonalReservationsOnly"

    var displayPersonalReservationsOnly: Boolean
        get() = window.sessionStorage.getItem(reservationState)?.toBoolean() ?: true
        set(value) = window.sessionStorage.setItem(reservationState, value.toString())

    fun clear() {
        window.sessionStorage.removeItem(reservationState)
    }
}