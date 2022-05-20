package components.reservation

import kotlinx.datetime.Clock
import model.Device
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.useState
import utils.converters.YYYY_MM_DD
import kotlin.js.Date

external interface ReservationCreateProps : Props {
    var device: Device
    var onCreateReservation: (deviceId: Int, from: Long, to: Long) -> Unit
}

val ReservationCreateComponent = FC<ReservationCreateProps> { props ->

    val (startDate, setStartDate) = useState(Clock.System.now().YYYY_MM_DD())
    val (endDate, setEndDate) = useState(Clock.System.now().YYYY_MM_DD())

    val startChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setStartDate(it.target.value)
    }

    val endChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setEndDate(it.target.value)
    }

    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        props.onCreateReservation(props.device.id,
            Date(startDate).getTime().toLong(),
            Date(endDate).getTime().toLong())
    }

    form {
        onSubmit = submitHandler

        div {
            +"Eszköz foglalása:"
            +"${props.device.name} \t ${props.device.desc}" //TODO: check avaibility for selected period
        }

        input {
            type = InputType.date
            value = startDate
            onChange = startChangeHandler
        }

        input {
            type = InputType.date
            value = endDate
            onChange = endChangeHandler
        }

        button {
            type = ButtonType.submit
            +"Reserve"
        }
    }
}