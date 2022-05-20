package hu.bme.aut.application.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*


object JwtConfig {
    private const val SECRET = "65adff1561fsg6fd51fgf5h61gga1d6fafsd61fgs" //TODO: should be loaded from a config file https://ktor.io/docs/configurations.html#environment-variables
    private const val ISSUER = "eszkozkolcsonzo-app"
    private const val AUDIENCE = "eszkozkolcsonzo-app"

    const val claim_ID = "id"
    const val claim_NAME = "name"
    const val claim_EMAIL = "email"
    const val claim_PRIV = "priv"

    private const val validityInMs = 3600_000 * 3 // 3 hours
    private val algorithm = Algorithm.HMAC256(SECRET)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    fun createAccessToken(id: Int, name: String, email: String, privilege: String): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(claim_ID, id)
        .withClaim(claim_NAME, name)
        .withClaim(claim_EMAIL, email)
        .withClaim(claim_PRIV, privilege)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}