package components.leases

import model.Lease
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.key

external interface LeaseListItemProps : Props {
    var lease: Lease
}

val LeaseListItem = FC<LeaseListItemProps> { props ->
    li {
        key = props.lease.toString()

        div {
            p {
                +"Lease ${props.lease.id} is made for reservation ${props.lease.reservationId}"
            }
            p {
                +"Handler: ${props.lease.handlerUserId} \t"
                +"Receiver: ${props.lease.requesterUserId}"
            }
        }
    }
}