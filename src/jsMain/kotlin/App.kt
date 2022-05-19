import components.TestComponent
import components.device.DeviceList
import components.page.PageHeaderComponent
import components.reservation.ReservationList
import components.user.LoginComponent
import components.user.RegisterComponent
import csstype.Margin
import utils.path.AppPath
import react.*
import react.css.css
import react.dom.html.ReactHTML.div
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter

val App = FC<Props> {

    PageHeaderComponent {}

    div {
        css {
            margin = Margin("8px")
        }
        BrowserRouter {
            Routes {
                Route {
                    path = AppPath.devices
                    element = DeviceList.create()
                }
                Route {
                    path = AppPath.reservations
                    element = ReservationList.create()
                }
                Route {
                    path = AppPath.leases
                    element = TestComponent.create()
                }
                Route {
                    path = AppPath.register
                    element = RegisterComponent.create()
                }
                Route {
                    path = AppPath.login
                    element = LoginComponent.create()
                }
            }
        }
    }
}