package hu.bme.aut.application.security

import io.ktor.auth.*

data class UserIdPrincipal(val id: Int): Principal