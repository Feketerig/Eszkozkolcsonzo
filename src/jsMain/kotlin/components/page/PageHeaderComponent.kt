package components.page

import csstype.*
import model.User
import react.FC
import react.Props
import react.css.css
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.nav
import utils.browser.AppState
import utils.browser.PageNavigator
import utils.browser.TokenStore


val PageHeaderComponent = FC<Props> {
    nav {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
            alignItems = AlignItems.center
            marginBottom = Margin("2em")
            backgroundColor = Color("#ad0c0c")
            color = Color("#fff")
            position = Position.sticky
        }

        when (TokenStore.getUserPrivilege()) {
            User.Privilege.User -> div {
                PageHeaderButton {
                    title = "Eszközök"
                    navigation = { PageNavigator.toDevices() }
                }
                PageHeaderButton {
                    title = "Foglalásaim"
                    navigation = {
                        AppState.displayPersonalReservationsOnly = true
                        PageNavigator.toReservations()
                    }
                }
                PageHeaderButton {
                    title = "Kijelentkezés"
                    navigation = { PageNavigator.toLogout() }
                }
            }
            User.Privilege.Handler -> div {
                PageHeaderButton {
                    title = "Eszközök"
                    navigation = { PageNavigator.toDevices() }
                }
                PageHeaderButton {
                    title = "Összes Foglalás"
                    navigation = {
                        AppState.displayPersonalReservationsOnly = false
                        PageNavigator.toReservations()
                    }
                }
                PageHeaderButton {
                    title = "Kiadások"
                    navigation = { PageNavigator.toLeases() }
                }
                PageHeaderButton {
                    title = "Foglalásaim"
                    navigation = {
                        AppState.displayPersonalReservationsOnly = true
                        PageNavigator.toReservations()
                    }
                }
                PageHeaderButton {
                    title = "Kijelentkezés"
                    navigation = { PageNavigator.toLogout() }
                }
            }
            User.Privilege.Admin -> div {
                PageHeaderButton {
                    title = "Eszközök"
                    navigation = { PageNavigator.toDevices() }
                }
                PageHeaderButton {
                    title = "Összes Foglalás"
                    navigation = {
                        AppState.displayPersonalReservationsOnly = false
                        PageNavigator.toReservations()
                    }
                }
                PageHeaderButton {
                    title = "Kiadások"
                    navigation = { PageNavigator.toLeases() }
                }
                PageHeaderButton {
                    title = "Foglalásaim"
                    navigation = {
                        AppState.displayPersonalReservationsOnly = true
                        PageNavigator.toReservations()
                    }
                }
                /*PageHeaderButton {
                    title = "Felhasználók"
                    navigation = { PageNavigator.toUsers() }
                }*/
                PageHeaderButton {
                    title = "Kijelentkezés"
                    navigation = { PageNavigator.toLogout() }
                }
            }
            null -> div {
                PageHeaderButton {
                    title = "Bejelentkezés"
                    navigation = { PageNavigator.toLogin() }
                }
            }
        }

        div {
            css {
                paddingRight = Padding("1em")
            }
            h1 {
                +"Eszközkölcsönző"
            }
        }
    }
}