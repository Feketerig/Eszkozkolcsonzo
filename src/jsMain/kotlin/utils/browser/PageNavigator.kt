package utils.browser

import kotlinx.browser.window
import utils.AppState
import utils.path.AppPath

object PageNavigator {
    fun toDevices() { window.location.href = window.origin + AppPath.devices }
    fun toLeases() { window.location.href = window.origin + AppPath.leases }
    fun toLogin() { window.location.href = window.origin + AppPath.login }
    fun toRegistration() { window.location.href = window.origin + AppPath.register }
    fun toReservations() { window.location.href = window.origin + AppPath.reservations }
    fun toLogout() {
        TokenStore.setJwtToken("")
        AppState.clear()
        window.location.href = window.origin + AppPath.login
    }
}