package utils.converters

import kotlinx.datetime.Instant
import kotlinx.datetime.toJSDate
import kotlin.js.Date

fun Instant.YYYY_MM_DD(): String {
    return toJSDate().YYYY_MM_DD()
}

fun Date.YYYY_MM_DD(): String {
    return getFullYear().toString().padStart(4, '0') + "-" +
            (getMonth()+1).toString().padStart(2, '0') + "-" +
            getDate().toString().padStart(2, '0')
}