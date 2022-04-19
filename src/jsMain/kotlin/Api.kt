import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window
import model.Device

val endpoint = window.location.origin

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getDeviceList(): List<Device>{
    return jsonClient.get(endpoint + Device.path)
}

suspend fun deleteDevice(id: Int) {
    return jsonClient.delete(endpoint + Device.path + "/${id}")
}

suspend fun addDevice(device: Device) {
    return jsonClient.post(endpoint + Device.path){
        contentType(ContentType.Application.Json)
        body = device
    }
}