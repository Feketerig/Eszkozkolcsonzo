package hu.bme.aut.application.security

import io.ktor.auth.*

data class UserAuthPrincipal(val id: Int, val name: String, val email: String, val privilege: String): Principal