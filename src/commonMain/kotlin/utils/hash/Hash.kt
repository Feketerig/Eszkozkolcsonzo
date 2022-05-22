package utils.hash

import com.soywiz.krypto.encoding.Base64
import com.soywiz.krypto.sha256
import io.ktor.utils.io.core.*

fun String.sha256(): String = this.toByteArray().sha256().hexLower

fun String.base64Decode(): String = Base64.decode(this).decodeToString()