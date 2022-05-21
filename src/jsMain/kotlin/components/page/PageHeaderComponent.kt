package components.page

import csstype.*
import react.FC
import react.Props
import react.css.css
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.nav
import utils.browser.PageNavigator


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

        div {
            PageHeaderButton {
                title = "Eszközök"
                navigation = { PageNavigator.toDevices() }
            }
            PageHeaderButton {
                title = "Foglalások"
                navigation = { PageNavigator.toReservations() }
            }
            PageHeaderButton {
                title = "Bejelentkezés"
                navigation = { PageNavigator.toLogin() }
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