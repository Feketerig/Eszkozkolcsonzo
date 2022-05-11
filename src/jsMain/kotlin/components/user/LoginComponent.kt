package components.user

import checkUserLogin
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.useState
import utils.components.LabeledInputField
import utils.converters.sha256

private val scope = MainScope()

external interface LoginComponentProps : Props

val LoginComponent = FC<LoginComponentProps> { props ->
    val (email, setEmail) = useState("")
    val (password, setPassword) = useState("")

    form {
        div {
            LabeledInputField {
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
        }

        onSubmit = {
            it.preventDefault()
            scope.launch {
                checkUserLogin(email, password.sha256())
                //TODO save the token, and navigate to devices
            }
        }

        ReactHTML.button {
            type = ButtonType.submit
            +"Belépés"
        }
    }
}