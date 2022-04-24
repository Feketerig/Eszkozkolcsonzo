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

private val scope = MainScope()
private var lastID = 0                  //TODO create a viable ID generator

external interface DeviceListProps : Props

val DeviceList = FC<DeviceListProps> {
    var deviceList by useState(emptyList<Device>())

    useEffectOnce {
        scope.launch {
            deviceList = getDeviceList()
        }
    }


    ul {
        deviceList.forEach { item ->
            DeviceListItem {
                device = item
                onDelete = { device ->
                    MainScope().launch {
                        deleteDevice(device.id)
                        deviceList = getDeviceList()
                    }
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

    ReservationCreateComponent {
        device = Device (100, "Kutyaház", "egy kutyának", true)
        onCreateReservation = { reservation ->
            scope.launch {
                addReservation(reservation)
            }
        }
    }
}