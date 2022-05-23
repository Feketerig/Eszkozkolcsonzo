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

external interface ReservationListItemProps : Props {
    var reservation: Reservation
    var onDelete: (Reservation) -> Unit
}

val ReservationListItem = FC<ReservationListItemProps> { props ->
    li {
        key = props.reservation.toString()

        div {
            p {
                +"Device ${props.reservation.deviceId} reserved by ${props.reservation.userId} "
                +"from: ${props.reservation.startDate} \t to: ${props.reservation.endDate}"
            }
        }

        if (TokenStore.getUserPrivilege() != User.Privilege.User) {
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