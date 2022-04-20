package components

import react.FC
import react.Props
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.h1


val TestComponent = FC<Props> { props ->
    h1 {
        +"Test Component"
    }
    p {
        +"Bújj bújj zöld ág, zöld levelecske"
    }
}