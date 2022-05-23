package components.reservation

import addReservation
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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
import react.dom.html.ReactHTML.p
import react.useState
import utils.converters.YYYY_MM_DD
import kotlin.js.Date

external interface ReservationCreateProps : Props {
    var device: Device
}

val ReservationCreateComponent = FC<ReservationCreateProps> { props ->

    val (startDate, setStartDate) = useState(Clock.System.now().YYYY_MM_DD())
    val (endDate, setEndDate) = useState(Clock.System.now().YYYY_MM_DD())
    val (message, setMessage) = useState("")

    val startChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setStartDate(it.target.value)
        setMessage("")
    }

    val endChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setEndDate(it.target.value)
        setMessage("")
        //TODO check if device can be reserved for this period in real time
    }

    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        MainScope().launch {
            try {
                addReservation(props.device.id, Date(startDate).getTime().toLong(), Date(endDate).getTime().toLong())
                setMessage("Sikeres Foglalás!")
            } catch (e: Exception) {
                setMessage("Nem sikerült a foglalás!")
            }
        }
    }

    form {
        onSubmit = submitHandler

        div {
            +"Eszköz foglalása:"
            +"${props.device.name} \t ${props.device.desc}"
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

    p {
        +message
    }
}