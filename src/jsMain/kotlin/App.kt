import components.device.DeviceList
import kotlinx.coroutines.MainScope
import react.*
import react.dom.html.ReactHTML

private val scope = MainScope()

val App = FC<Props> {

    ReactHTML.h1 {
        +"Eszközkölcsönző"
    }

    DeviceList {}
}