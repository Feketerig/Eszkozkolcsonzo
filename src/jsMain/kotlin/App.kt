import components.TestComponent
import components.device.DeviceList
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
                    path = AppPath.leases
                    element = TestComponent.create()
                }
            }

        }
    }
}