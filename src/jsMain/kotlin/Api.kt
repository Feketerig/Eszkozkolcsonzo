import utils.path.ServerApiPath
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window
import model.Device
import model.Reservation

val endpoint = window.location.origin

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getDeviceList(): List<Device>{
    return jsonClient.get(endpoint + ServerApiPath.devicePath)
}

suspend fun deleteDevice(id: Int) {
    return jsonClient.delete(endpoint + ServerApiPath.devicePath + "/${id}")
}

suspend fun addDevice(device: Device) {
    return jsonClient.post(endpoint + ServerApiPath.devicePath) {
        contentType(ContentType.Application.Json)
        body = device
    }
}

suspend fun getReservationList(): List<Reservation>{
    return jsonClient.get(endpoint + ServerApiPath.reservationPath)
}

suspend fun addReservation(reservation: Reservation) {
    return jsonClient.post(endpoint + ServerApiPath.reservationPath) {
        contentType(ContentType.Application.Json)
        body = reservation
    }
}