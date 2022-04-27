package components.device

import model.Device
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.li
import react.key

external interface DeviceListItemProps : Props {
    var device: Device
    var onDelete: (Device) -> Unit
    var onSelect: (Device) -> Unit
}

val DeviceListItem = FC<DeviceListItemProps> { props ->
    li {
        key = props.device.toString()
        +"${props.device.name} \t ${props.device.desc} \t ${props.device.available}"

        button {
            +"Delete"
            onClick = {
                it.preventDefault()
                props.onDelete(props.device)
            }
        }

        button {
            +"Reserve"
            onClick = {
                it.preventDefault()
                props.onSelect(props.device)
            }
        }
    }
}