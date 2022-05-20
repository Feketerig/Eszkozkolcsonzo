
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.browser.window
import model.Device
import model.Reservation
import utils.browser.TokenStore
import utils.exceptions.*
import utils.path.ServerApiPath

val endpoint = window.location.origin

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
    HttpResponseValidator {
        handleResponseException { exception ->
            if (exception !is ClientRequestException) return@handleResponseException
            when (exception.response.status) {
                HttpStatusCode.Unauthorized -> throw UnauthorizedException()
                HttpStatusCode.Forbidden -> throw ForbiddenException()
                HttpStatusCode.InternalServerError -> throw ServerErrorException()
                HttpStatusCode.NotFound -> throw NotFoundException()
                HttpStatusCode.Conflict -> throw ConflictException()
                else -> throw Exception("Unknown error + ${exception.response.status.value}")
            }
        }
    }
}

suspend fun getDeviceList(): List<Device>{
    val response: HttpResponse = jsonClient.get(endpoint + ServerApiPath.devicePath) {
        header("Authorization", "Bearer " + TokenStore.get())
    }
    return response.receive()
}

suspend fun deleteDevice(id: Int): Unit {
    return jsonClient.delete(endpoint + ServerApiPath.devicePath + "/${id}") {
        header("Authorization", "Bearer " + TokenStore.get())
    }
}

suspend fun addDevice(name: String, description: String): Unit {
    return jsonClient.post(endpoint + ServerApiPath.devicePath) {
        contentType(ContentType.Application.Json)
        header("Authorization", "Bearer " + TokenStore.get())
        parameter("name", name)
        parameter("desc", description)
    }
}

suspend fun getReservationList(): List<Reservation> {
    return jsonClient.get(endpoint + ServerApiPath.reservationPath) {
        header("Authorization", "Bearer " + TokenStore.get())
    }
}

suspend fun addReservation(deviceId: Int, from: Long, to: Long): Unit {
    return jsonClient.post(endpoint + ServerApiPath.reservationPath) {
        contentType(ContentType.Application.Json)
        header("Authorization", "Bearer " + TokenStore.get())
        parameter("deviceid", deviceId)
        parameter("from", from)
        parameter("to", to)
    }
}

suspend fun registerUser(name: String, email: String, phone: String, address: String, pwHash: String): Unit {
    return jsonClient.post(endpoint + ServerApiPath.userPath) {
        contentType(ContentType.Application.Json)
        header("Authorization", "Bearer " + TokenStore.get())
        parameter("name", name)
        parameter("email", email)
        parameter("phone", phone)
        parameter("address", address)
        parameter("pwHash", pwHash)
    }
}

/**
 * returns the token for the user
 */
suspend fun loginAsUser(email: String, pwHash: String): String {
    return jsonClient.post(endpoint + ServerApiPath.userPath + "/login") {
        contentType(ContentType.Application.Json)
        header("Authorization", "Bearer " + TokenStore.get())
        body = "$email|$pwHash"
    }
}