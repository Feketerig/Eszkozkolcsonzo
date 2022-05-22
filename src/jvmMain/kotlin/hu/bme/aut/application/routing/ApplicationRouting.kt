package hu.bme.aut.application.routing

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import utils.path.AppPath

fun Application.pages() {
    routing {
        route("/") {
            authenticate("require-login") {
                get(AppPath.devices + "/{...}", getDefaultPage)
                get(AppPath.leases + "/{...}", getDefaultPage)
                get(AppPath.reservations + "/{...}", getDefaultPage)
            }
            get(getDefaultPage)
            get(AppPath.login + "/{...}", getDefaultPage)
            get(AppPath.register + "/{...}", getDefaultPage)
        }
    }
}

val getDefaultPage: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
    call.respondText(
        this::class.java.classLoader.getResource("index.html")!!.readText(),
        ContentType.Text.Html
    )
}