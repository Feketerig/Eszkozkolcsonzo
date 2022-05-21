package components.page

import csstype.*
import react.FC
import react.Props
import react.css.css
import react.dom.html.ReactHTML.a

external interface HeaderButtonProps : Props {
    var title: String
    var navigation: () -> Unit
}

val PageHeaderButton = FC<HeaderButtonProps> {props ->
    a {
        css {
            hover {
                backgroundColor = Color("#cd3c3c")
            }
            float = Float.left
            display = Display.block
            textAlign = TextAlign.center
            padding = Padding("10px 14px")
        }

        onClick = { props.navigation() }
        +props.title
    }
}
