package components.page

import csstype.*
import react.FC
import react.Props
import react.css.css
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.nav
import utils.browser.PageNavigator


val PageHeaderComponent = FC<Props> {
    nav {
        css {
            boxSizing = BoxSizing.borderBox
            width = Length("100%")
            display = Display.flex
            justifyContent = JustifyContent.flexStart
            alignItems = AlignItems.baseline
            marginBottom = Margin("2em")
            backgroundColor = Color("#ad0c0c")
            color = Color("#fff")

            position = Position.sticky
        }

        a {
            css {
                hover {
                    backgroundColor = Color("#cd3c3c")
                }
                float = Float.left
                display = Display.block
                textAlign = TextAlign.center
                padding = Padding("10px 14px")
            }
            onClick = {
                PageNavigator.toDevices()
            }
            +"Eszközök"
        }
        a {
            css {
                hover {
                    backgroundColor = Color("#cd3c3c")
                }
                float = Float.left
                display = Display.block
                textAlign = TextAlign.center
                padding = Padding("10px 14px")
            }
            onClick = {
                PageNavigator.toLogin()
            }
            +"Bejelentkezés"
        }

        div {
            css {
                justifySelf = JustifySelf.flexEnd
            }
            h1 {
                +"Eszközkölcsönző"
            }
        }
    }
}