package hu.bme.aut.application

import database.Database
import database.WrongIdException
import hu.bme.aut.application.security.JwtConfig
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import utils.path.ServerApiPath

fun Application.deviceApi(database: Database){
    routing {
        route(ServerApiPath.devicePath) {
            get() {
                call.respond(database.getAllDevices())
            }
            get("/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    call.respond(database.getDevice(id))
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            post() {
                database.addDevice(call.receive())
                call.respond(HttpStatusCode.OK)
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                database.deleteDevice(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun Application.leaseApi(database: Database) {
    routing {
        route(ServerApiPath.leasePath) {
            get() {
                call.respond(database.getActiveLeases())
            }
            get("/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    call.respond(database.getLease(id))
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            get("/reservation/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    call.respond(database.getLeaseIdByReservationId(id))
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            put("/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    database.deactivateLease(id)
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            post() {
                database.addLease(call.receive())
                call.respond(HttpStatusCode.OK)
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                database.deleteLease(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun Application.reservationApi(database: Database) {
    routing {
        route(ServerApiPath.reservationPath) {
            get() {
                call.respond(database.getAllReservations())
            }
            get("/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    call.respond(database.getReservation(id))
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            get("/user/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    call.respond(database.getAllReservationByUserId(id))
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            get("/device/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    call.respond(database.getReservationByDeviceId(id))
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            post() {
                database.addReservation(call.receive())
                call.respond(HttpStatusCode.OK)
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                database.deleteReservation(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun Application.userApi(database: Database) {
    routing {
        route(ServerApiPath.userPath) {
            get() {
                val email = call.request.queryParameters["email"] ?: error("Invalid username")
                call.respond(database.getUserByEmail(email))
            }
            get("/{id}") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    call.respond(database.getUserById(id))
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            get("/{id}/name") {
                try {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    call.respond(database.getUserNameById(id))
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            post() {
                database.addUser(call.receive())
                call.respond(HttpStatusCode.OK)
            }
            post("/login") {
                try {
                    val msg = call.receive<String>().drop(1).dropLast(1).split("|")
                    val user = database.getUserByEmail(msg[0])
                    if (user.password_hash == msg[1]) {
                        val token = JwtConfig.createAccessToken(user.id)
                        call.respond(token)
                        //call.respond(hashMapOf("token" to token))
                        //call.respond(user.auth_token)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                } catch (e: WrongIdException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}

fun Application.authtest() {
    routing {
        authenticate {
            route("/asd") {
                get() {
                    call.respond("yaaaay")
                }
            }
        }
    }
}