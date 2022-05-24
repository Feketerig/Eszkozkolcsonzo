package components.device

import addDevice
import components.reservation.ReservationCreateComponent
import deleteDevice
import getDeviceList
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Device
import react.FC
import react.Props
import react.dom.html.ReactHTML.ul
import react.useEffectOnce
import react.useState
import utils.browser.PageNavigator
import utils.exceptions.ConflictException
import utils.exceptions.UnauthorizedException

private val scope = MainScope()

val DeviceList = FC<Props> {
    var deviceList by useState(emptyList<Device>())
    val (selectedDevice, setSelected) = useState<Device?>(null)

    useEffectOnce {
        scope.launch {
            try {
                deviceList = getDeviceList()
            } catch (e: UnauthorizedException) {
                PageNavigator.toLogin() //TODO move login redirect to somewhere more general
            }
        }
    }

    ul {
        deviceList.forEach { item ->
            DeviceListItem {
                device = item
                onDelete = { device ->
                    MainScope().launch {
                        try {
                            deleteDevice(device.id)
                        } catch (e: ConflictException) {
                            window.alert("This device is reserved, can not be deleted")
                        }
                        if (selectedDevice != null && selectedDevice.id == device.id) {
                            setSelected(null)
                        }
                        deviceList = getDeviceList()
                    }
                }
                onSelect = { device ->
                    setSelected(device)
                }
            }
        }
    }

    NewDeviceInput {
        onSubmit = { name, desc ->
            scope.launch {
                addDevice(name, desc)
                deviceList = getDeviceList()
            }
        }
    }

    if (selectedDevice != null){
        ReservationCreateComponent {
            device = selectedDevice
        }
    }
}