package components.user

import components.LabeledInputField
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import loginAsUser
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.p
import react.useState
import registerUser
import utils.browser.PageNavigator
import utils.browser.TokenStore
import utils.exceptions.ConflictException
import utils.hash.sha256

private val scope = MainScope()

external interface RegisterComponentProps : Props

val RegisterComponent = FC<RegisterComponentProps> { props ->
    val (email, setEmail) = useState("")
    val (password, setPassword) = useState("")
    val (name, setName) = useState("")
    val (phone, setPhone) = useState("")
    val (address, setAddress) = useState("")
    val (message, setMessage) = useState("")

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

        p {
            +message
        }

        onSubmit = {
            it.preventDefault()
            scope.launch {
                try {
                    async { registerUser(name, email, phone, address, password.sha256()) }.await()
                    async { TokenStore.setJwtToken(loginAsUser(email, password.sha256())) }.await()
                    setName(""); setEmail(""); setPassword(""); setPhone(""); setAddress("")
                    setMessage("")
                    PageNavigator.toDevices()
                } catch (e: ConflictException) {
                    setMessage("Ez az email cím már foglalt!")
                }
            }
        }

        button {
            type = ButtonType.submit
            +"Regisztráció"
        }
    }

    div {
        button {
            onClick = { PageNavigator.toLogin() }
            +"Már van fiókom, belépek"
        }
    }
}