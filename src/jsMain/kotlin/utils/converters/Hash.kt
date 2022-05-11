package utils.converters

import com.soywiz.krypto.sha256
import io.ktor.utils.io.core.*

fun String.sha256(): String = this.toByteArray().sha256().hexLower

fun generateAuthToken(name: String, password: String): String {
    return (name + "51g645af56sfg15ad1f6gd1561fa651ht6zs56d" + password).sha256()
    //TODO: this should be updated to a JWT token, from being basically an API key
}