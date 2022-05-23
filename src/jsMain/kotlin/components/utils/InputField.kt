package components.utils

import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.html.InputType
import react.dom.html.ReactHTML

external interface InputFieldProps : Props {
    var description: String
    var value: String
    var onChange: ChangeEventHandler<HTMLInputElement>?
    var inputType: InputType?
    var required: Boolean?
}

val LabeledInputField = FC<InputFieldProps> { props ->
    ReactHTML.div {
        ReactHTML.label {
            htmlFor = props.description
            +props.description
        }
        ReactHTML.input {
            type = props.inputType ?: InputType.text
            required = props.required ?: true
            name = props.description

            onChange = props.onChange
            value = props.value
        }
    }
}