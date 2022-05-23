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
import utils.exceptions.UnauthorizedException

private val scope = MainScope()

external interface ReservationListProps : Props

val ReservationList = FC<ReservationListProps> {
    var reservationList by useState(emptyList<Reservation>())

    useEffectOnce {
        scope.launch {//TODO This is great, it works, but it shouldnt be here. redirect to login should happen on the server immediately
                      //TODO This kind of solution is only for low access level, not generally having to be logged in.
                      //TODO Also this should be in a function, not to spam it everywhere
            try {
                reservationList = getReservations()
            } catch (e: UnauthorizedException) {
                PageNavigator.toLogin()
            }
        }
    }


    ul {
        reservationList.forEach { item ->
            ReservationListItem {
                reservation = item
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