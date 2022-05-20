package hu.bme.aut.application.routing.utils

import hu.bme.aut.application.security.UserAuthPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import model.User.Privilege

suspend inline fun PipelineContext<Unit, ApplicationCall>.requireAccessLevel(privilege: Privilege, body: () -> Unit) {
    if (checkAccessLevel((call.authentication.principal as UserAuthPrincipal), privilege)) {
        body()
    }
    else {
        call.respond(HttpStatusCode.Forbidden)
    }
}

fun checkAccessLevel(principal: UserAuthPrincipal, required: Privilege): Boolean {
    return when (required) {
        Privilege.Admin -> Privilege.Admin.compare(principal.privilege)
        Privilege.Handler -> Privilege.Handler.compare(principal.privilege) || Privilege.Admin.compare(principal.privilege)
        Privilege.User -> true
    }

}

private fun Privilege.compare(other: String): Boolean = toString() == other