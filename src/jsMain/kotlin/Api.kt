import utils.path.ServerApiPath
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window
import model.Device
import model.Reservation
import model.User
import utils.session.TokenStore

val endpoint = window.location.origin

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getDeviceList(): List<Device>{
    return jsonClient.get(endpoint + ServerApiPath.devicePath) {
        header("Authorization", "Bearer " + TokenStore.get())
    }
}

suspend fun deleteDevice(id: Int): Unit {
    return jsonClient.delete(endpoint + ServerApiPath.devicePath + "/${id}") {
        header("Authorization", "Bearer " + TokenStore.get())
    }
}

suspend fun addDevice(device: Device): Unit {
    return jsonClient.post(endpoint + ServerApiPath.devicePath) {
        contentType(ContentType.Application.Json)
        header("Authorization", "Bearer " + TokenStore.get())
        body = device
    }
}

suspend fun getReservationList(): List<Reservation> {
    return jsonClient.get(endpoint + ServerApiPath.reservationPath) {
        header("Authorization", "Bearer " + TokenStore.get())
    }
}

suspend fun addReservation(reservation: Reservation): Unit {
    return jsonClient.post(endpoint + ServerApiPath.reservationPath) {
        contentType(ContentType.Application.Json)
        header("Authorization", "Bearer " + TokenStore.get())
        body = reservation
    }
}

suspend fun registerUser(user: User): Unit {
    return jsonClient.post(endpoint + ServerApiPath.userPath) {
        contentType(ContentType.Application.Json)
        header("Authorization", "Bearer " + TokenStore.get())
        body = user
    }
}

/**
 * returns the token for the user
 */
suspend fun checkUserLogin(email: String, pwHash: String): String {
    return jsonClient.post(endpoint + ServerApiPath.userPath + "/login") {
        contentType(ContentType.Application.Json)
        header("Authorization", "Bearer " + TokenStore.get())
        body = "$email|$pwHash"
    }
}