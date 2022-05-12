package hu.bme.aut.application

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import utils.path.AppPath

fun Application.pages() {
    routing {
        route("/") {
            get(getDefaultPage)
            get(AppPath.devices + "/{...}", getDefaultPage)
            get(AppPath.leases + "/{...}", getDefaultPage)
            get(AppPath.login + "/{...}", getDefaultPage)
            get(AppPath.register + "/{...}", getDefaultPage)
            get(AppPath.reservations + "/{...}", getDefaultPage)
        }
    }
}

val getDefaultPage: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
    call.respondText(
        this::class.java.classLoader.getResource("index.html")!!.readText(),
        ContentType.Text.Html
    )
}