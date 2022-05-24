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
        Privilege.Admin -> Privilege.Admin.eq(principal.privilege)
        Privilege.Handler -> Privilege.Handler.eq(principal.privilege) || Privilege.Admin.eq(principal.privilege)
        Privilege.User -> true
    }
}

private fun Privilege.eq(other: String): Boolean = toString() == other