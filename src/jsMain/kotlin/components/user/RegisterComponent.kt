package components.user

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.User
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.useState
import registerUser
import utils.components.LabeledInputField
import utils.converters.generateAuthToken
import utils.converters.sha256

private val scope = MainScope()

external interface RegisterComponentProps : Props

val RegisterComponent = FC<RegisterComponentProps> { props ->
    val (email, setEmail) = useState("")
    val (password, setPassword) = useState("")
    val (name, setName) = useState("")
    val (phone, setPhone) = useState("")
    val (address, setAddress) = useState("")

    form {
        div {
            LabeledInputField { //TODO: check for duplicate email
                description = "E-mail cím"
                inputType = InputType.email
                value = email
                onChange = { setEmail(it.target.value) }
            }
            LabeledInputField {
                description = "Jelszó"
                inputType = InputType.password
                value = password
                onChange = { setPassword(it.target.value) }
            }
            LabeledInputField {
                description = "Név"
                value = name
                onChange = { setName(it.target.value) }
            }
            LabeledInputField {
                description = "Telefon"
                value = phone
                onChange = { setPhone(it.target.value) }
            }
            LabeledInputField {
                description = "Cím"
                value = address
                onChange = { setAddress(it.target.value) }
            }
        }

        onSubmit = {
            it.preventDefault()
            val user = User(0, name, email, phone, address,
                User.Privilege.User,
                password.sha256(),
                generateAuthToken(name, password)
            )
            setName("")
            setEmail("")
            setPassword("")
            setPhone("")
            setAddress("")
            scope.launch {
                registerUser(user)
                //TODO log in with new user, and navigate do somewhere. Device list maybe?
            }
        }

        button {
            type = ButtonType.submit
            +"Regisztráció"
        }
    }
}