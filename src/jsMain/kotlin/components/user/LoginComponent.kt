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
import utils.browser.PageNavigator
import utils.browser.TokenStore
import utils.exceptions.NotFoundException
import utils.exceptions.UnauthorizedException
import utils.hash.sha256

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
                    async { TokenStore.setJwtToken(loginAsUser(email, password.sha256())) }.await()
                    setMessage("")
                    PageNavigator.toDevices()
                } catch (e: NotFoundException) {
                    setMessage("Ezzel az email címmel még senki sem regisztrált!")
                } catch (e: UnauthorizedException) {
                    setMessage("Helytelen jelszó!")
                }
            }
        }

        button {
            type = ButtonType.submit
            +"Belépés"
        }
    }

    div {
        button {
            onClick = { PageNavigator.toRegistration() }
            +"Még nincs fiókom, regisztrálok"
        }
    }
}