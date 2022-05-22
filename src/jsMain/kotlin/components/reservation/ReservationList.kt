package components.reservation

import getReservationListForCurrentUser
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Reservation
import react.FC
import react.Props
import react.dom.html.ReactHTML.ul
import react.useEffectOnce
import react.useState

private val scope = MainScope()

external interface ReservationListProps : Props

val ReservationList = FC<ReservationListProps> {
    var reservationList by useState(emptyList<Reservation>())

    useEffectOnce {
        scope.launch {
            reservationList = getReservationListForCurrentUser()
        }
    }


    ul {
        reservationList.forEach { item ->
            ReservationListItem {
                reservation = item
            }
        }
    }
}