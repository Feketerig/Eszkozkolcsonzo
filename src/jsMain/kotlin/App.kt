import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Device
import react.*
import react.dom.html.ReactHTML

private val scope = MainScope()

val App = FC<Props> {
    var deviceList by useState(emptyList<Device>())

    useEffectOnce {
        scope.launch {
            deviceList = getDeviceList()
        }
    }

    ReactHTML.h1 {
        +"Eszközkölcsönző"
    }
    ReactHTML.ul {
        deviceList.forEach { item ->
            ReactHTML.li {
                key = item.toString()
                +"${item.name} \t ${item.desc} \t ${item.available}"
            }
            onClick = {
                scope.launch {
                    deleteDevice(item.id)
                    deviceList = getDeviceList()
                }
            }
        }
    }
}