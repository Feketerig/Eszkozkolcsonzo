package components.device

import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input


external interface DeviceInputProps : Props {
    var onSubmit: (name: String, description: String) -> Unit
}

val NewDeviceInput = FC<DeviceInputProps> { props ->
    val (name, setName) = useState("")
    val (desc, setDesc) = useState("")

    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        props.onSubmit(name, desc)
        setName("")
        setDesc("")
    }

    val nameChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setName(it.target.value)
    }

    val descriptionChangeHandler: ChangeEventHandler<HTMLInputElement> = {
        setDesc(it.target.value)
    }

    form {
        onSubmit = submitHandler

        input {
            type = InputType.text
            onChange = nameChangeHandler
            value = name
        }
        input {
            type = InputType.text
            onChange = descriptionChangeHandler
            value = desc
        }

        button {
            type = ButtonType.submit
            +"Hozzáadás"
        }
    }

}