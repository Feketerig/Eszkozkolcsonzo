import components.TestComponent
import components.device.DeviceList
import components.reservation.ReservationCreateComponent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.Device
import utils.path.AppPath
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter

val App = FC<Props> {

    BrowserRouter {
        div {
            h1 {
                +"Eszközkölcsönző"
            }

            Routes {
                Route {
                    path = AppPath.devices
                    element = DeviceList.create()
                }
                Route {
                    path = AppPath.reservations
                    element = ReservationCreateComponent.create {
                        device = Device(999, "bing", "boing", true) //TODO: this should be an id, and the device should be queried from inside of the component
                        onCreateReservation = { reservation ->
                            MainScope().launch {
                                addReservation(reservation)
                            }
                        }
                    }
                }
                Route {
                    path = AppPath.leases
                    element = TestComponent.create()
                }
            }
        }
    }
}