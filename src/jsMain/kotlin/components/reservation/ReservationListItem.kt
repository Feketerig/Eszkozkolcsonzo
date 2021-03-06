package components.reservation

import model.Reservation
import model.User
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.key
import utils.browser.TokenStore
import utils.converters.YYYY_MM_DD
import kotlin.js.Date

external interface ReservationListItemProps : Props {
    var reservation: Reservation
    var forUser: Int?
    var onDelete: (Reservation) -> Unit
}

val ReservationListItem = FC<ReservationListItemProps> { props ->
    li {
        key = props.reservation.toString()

        div {
            p {
                +"Device ${props.reservation.deviceId} reserved by ${props.reservation.userId} "
                +"from: ${Date(props.reservation.startDate).YYYY_MM_DD()} \t to: ${Date(props.reservation.endDate).YYYY_MM_DD()}"
            }
        }

        if (TokenStore.getUserPrivilege() != User.Privilege.User || (props.forUser ?: -1) == props.reservation.userId ) {
            button {
                +"Delete"
                onClick = {
                    it.preventDefault()
                    props.onDelete(props.reservation)
                }
            }
        }
    }
}