package hu.bme.aut.application.security

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.response.*
import utils.path.AppPath

fun Application.configureSecurity() {
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            validate {
                val claim = it.payload.getClaim(JwtConfig.claim_ID).asInt()
                if (claim != null) {
                    UserIdPrincipal(claim)
                }
                else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respondRedirect(AppPath.login)
            }
        }
    }
}