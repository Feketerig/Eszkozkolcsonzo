
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window
import model.Device
import model.Lease
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
                HttpStatusCode.PreconditionFailed -> throw PreconditionFailedException()
                else -> throw Exception("Unknown error + ${exception.response.status.value}")
            }
        }
    }
}

suspend fun getDeviceList(): List<Device>{
    return jsonClient.get(endpoint + ServerApiPath.devicePath) {
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
    }
}

suspend fun getDeviceAvailability(id: Int, from: Long, to: Long): Boolean {
    return jsonClient.get(endpoint + ServerApiPath.devicePath + "/${id}/available") {
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
        parameter("from", from)
        parameter("to", to)
    }
}

suspend fun deleteDevice(id: Int) {
    return jsonClient.delete(endpoint + ServerApiPath.devicePath + "/${id}") {
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
    }
}

suspend fun addDevice(name: String, description: String) {
    return jsonClient.post(endpoint + ServerApiPath.devicePath) {
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
        parameter("name", name)
        parameter("desc", description)
    }
}

suspend fun getActiveLeaseList(): List<Lease> {
    return jsonClient.get(endpoint + ServerApiPath.leasePath) {
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
    }
}

suspend fun getReservationList(): List<Reservation> {
    return jsonClient.get(endpoint + ServerApiPath.reservationPath) {
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
    }
}

suspend fun getReservationListForCurrentUser(): List<Reservation> {
    return jsonClient.get(endpoint + ServerApiPath.reservationPath + "/me") {
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
    }
}

suspend fun addReservation(deviceId: Int, from: Long, to: Long) {
    return jsonClient.post(endpoint + ServerApiPath.reservationPath) {
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
        parameter("deviceid", deviceId)
        parameter("from", from)
        parameter("to", to)
    }
}

suspend fun deleteReservation(id: Int) {
    return jsonClient.delete(endpoint + ServerApiPath.reservationPath + "/${id}"){
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
    }
}

suspend fun registerUser(name: String, email: String, phone: String, address: String, pwHash: String) {
    return jsonClient.post(endpoint + ServerApiPath.userPath) {
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
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
        header("Authorization", "Bearer " + TokenStore.getJwtToken())
        body = "$email|$pwHash"
    }
}