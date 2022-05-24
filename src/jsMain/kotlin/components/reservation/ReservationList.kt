package components.reservation

import deleteReservation
import getReservationList
import getReservationListForCurrentUser
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Reservation
import react.FC
import react.Props
import react.dom.html.ReactHTML.ul
import react.useEffectOnce
import react.useState
import utils.browser.AppState
import utils.browser.PageNavigator
import utils.browser.TokenStore
import utils.exceptions.UnauthorizedException

private val scope = MainScope()

val ReservationList = FC<Props> {
    var reservationList by useState(emptyList<Reservation>())
    val userId by useState(TokenStore.getUserId())

    useEffectOnce {
        scope.launch {
            try {
                reservationList = getReservations()
            } catch (e: UnauthorizedException) {
                PageNavigator.toLogin() //TODO move login redirect to somewhere more general
            }
        }
    }

    ul {
        reservationList.forEach { item ->
            ReservationListItem {
                reservation = item
                forUser = userId
                onDelete = { reservation ->
                    MainScope().launch {
                        deleteReservation(reservation.id)
                        reservationList = getReservations()
                    }
                }
            }
        }
    }
}

private suspend fun getReservations(): List<Reservation> {
    return if (AppState.displayPersonalReservationsOnly) {
        getReservationListForCurrentUser()
    } else {
        getReservationList()
    }
}