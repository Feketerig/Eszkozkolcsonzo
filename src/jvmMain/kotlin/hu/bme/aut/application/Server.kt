package hu.bme.aut.application

import database.MongoDB
import hu.bme.aut.application.security.configureSecurity
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main() {
    val mongoDB = MongoDB(database = KMongo.createClient().coroutine.getDatabase("eszkozkolcsonzo"))

    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        configureSecurity()
        install(Routing) {
            deviceApi(mongoDB)
            leaseApi(mongoDB)
            reservationApi(mongoDB)
            userApi(mongoDB)

            pages()

            authtest()

            static("/") {
                resources("")
            }
        }
    }.start(wait = true)
}