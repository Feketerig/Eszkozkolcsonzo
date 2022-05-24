package hu.bme.aut.application.security

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("basic-jwt") {
            verifier(JwtConfig.verifier)
            validate {
                val id = it.payload.getClaim(JwtConfig.claim_ID).asInt()
                val name = it.payload.getClaim(JwtConfig.claim_NAME).asString()
                val email = it.payload.getClaim(JwtConfig.claim_EMAIL).asString()
                val privilege = it.payload.getClaim(JwtConfig.claim_PRIV).asString()

                if (id != null && name != null && email != null && privilege != null) {
                    UserAuthPrincipal(id, name, email, privilege)
                }
                else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}