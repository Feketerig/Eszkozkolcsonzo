package components.user

import loginAsUser
import components.LabeledInputField
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
import react.dom.html.ReactHTML.p
import utils.hash.sha256
import utils.browser.PageNavigator
import utils.browser.TokenStore
import utils.exceptions.NotFoundException
import utils.exceptions.UnauthorizedException

private val scope = MainScope()

external interface LoginComponentProps : Props

val LoginComponent = FC<LoginComponentProps> { props ->
    val (email, setEmail) = useState("")
    val (password, setPassword) = useState("")
    val (message, setMessage) = useState("")

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

        p {
            +message
        }

        onSubmit = {
            it.preventDefault()
            scope.launch {
                try {
                    TokenStore.put(loginAsUser(email, password.sha256()))
                    setMessage("")
                    PageNavigator.toDevices()
                } catch (e: NotFoundException) {
                    setMessage("Ezzel az email címmel még senki sem regisztrált!")
                } catch (e: UnauthorizedException) {
                    setMessage("Helytelen jelszó!")
                }
            }
        }

        ReactHTML.button {
            type = ButtonType.submit
            +"Belépés"
        }
    }
}