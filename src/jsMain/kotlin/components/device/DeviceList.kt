package components.device

import addDevice
import addReservation
import components.reservation.ReservationCreateComponent
import deleteDevice
import getDeviceList
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Device
import react.FC
import react.Props
import react.dom.html.ReactHTML.ul
import react.useEffectOnce
import react.useState
import utils.browser.PageNavigator
import utils.exceptions.UnauthorizedException

private val scope = MainScope()
private var lastID = 0                  //TODO create a viable ID generator

external interface DeviceListProps : Props

val DeviceList = FC<DeviceListProps> {
    var deviceList by useState(emptyList<Device>())
    val (selectedDevice, setSelected) = useState<Device?>(null)

    useEffectOnce {
        scope.launch { //TODO This is great, it works, but it shouldnt be here. redirect to login should happen on the server immediately
                       //TODO This kind of solution is only for low access level, not generally having to be logged in.
                       //TODO Also this should be in a function, not to spam it everywhere
            try {
                deviceList = getDeviceList()
            } catch (e: UnauthorizedException) {
                PageNavigator.toLogin()
            }
        }
    }


    ul {
        deviceList.forEach { item ->
            DeviceListItem {
                device = item

                onDelete = { device ->
                    MainScope().launch {
                        deleteDevice(device.id)
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
            val device = Device(lastID++, name, desc, true)
            scope.launch {
                addDevice(device)
                deviceList = getDeviceList()
            }
        }
    }

    if (selectedDevice != null){
        ReservationCreateComponent {
            device = selectedDevice
            onCreateReservation = { reservation ->
                scope.launch {
                    addReservation(reservation)
                }
            }
        }
    }
}