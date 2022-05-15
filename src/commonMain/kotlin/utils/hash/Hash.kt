package utils.hash

import com.soywiz.krypto.sha256
import io.ktor.utils.io.core.*

fun String.sha256(): String = this.toByteArray().sha256().hexLower