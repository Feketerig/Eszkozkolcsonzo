package components.reservation

import model.Reservation
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.key

external interface ReservationListItemProps : Props {
    var reservation: Reservation
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

    }
}