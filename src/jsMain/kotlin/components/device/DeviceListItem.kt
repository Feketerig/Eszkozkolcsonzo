package components.device

import model.Device
import model.User
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.li
import react.key
import utils.browser.TokenStore

external interface DeviceListItemProps : Props {
    var device: Device
    var onDelete: (Device) -> Unit
    var onSelect: (Device) -> Unit
}

val DeviceListItem = FC<DeviceListItemProps> { props ->
    li {
        key = props.device.toString()
        +"${props.device.name} - ${props.device.desc}"

        if (TokenStore.getUserPrivilege() != User.Privilege.User) {
            button {
                +"Delete"
                onClick = {
                    it.preventDefault()
                    props.onDelete(props.device)
                }
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