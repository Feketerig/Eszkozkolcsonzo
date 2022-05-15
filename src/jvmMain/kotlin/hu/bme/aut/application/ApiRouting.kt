package hu.bme.aut.application

import database.Database
import database.WrongIdException
import hu.bme.aut.application.security.JwtConfig
import hu.bme.aut.application.security.UserIdPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import utils.path.ServerApiPath

fun Application.deviceApi(database: Database){
    routing {
        authenticate {
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
                    val name = call.parameters["name"] ?: error("device must have a name")
                    val desc = call.parameters["desc"] ?: ""
                    database.addDevice(Device(database.getNextDeviceId(), name, desc, true))
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
}

fun Application.leaseApi(database: Database) {
    routing {
        authenticate {
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
                    val resId = call.parameters["resId"]?.toInt() ?: error("reservation id must be specicied")
                    val kiado = call.parameters["kiado"]?.toInt() ?: error("user id must be specicied")
                    val atvevo = call.parameters["atvevo"]?.toInt() ?: error("user id must be specicied")
                    database.addLease(Lease(database.getNextLeaseId(), resId, kiado, atvevo, true))
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
}

fun Application.reservationApi(database: Database) {
    routing {
        authenticate {
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
                    val deviceid = call.parameters["deviceid"]?.toInt() ?: error("device must be specified")
                    val userid = (call.authentication.principal as UserIdPrincipal).id
                    val from = call.parameters["from"]?.toLong() ?: error("start date must be specified")
                    val to = call.parameters["to"]?.toLong() ?: error("end date must be specified")
                    database.addReservation(Reservation(database.getNextReservationId(), deviceid, from, to, userid))
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
}

fun Application.userApi(database: Database) {
    routing {
        authenticate {
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
            }
        }
        //Authentication not needed
        route(ServerApiPath.userPath) {
            post() {
                val name = call.parameters["name"] ?: error("user name must be specified")
                val email = call.parameters["email"] ?: error("user name must be specified")
                val phone = call.parameters["phone"] ?: ""
                val address = call.parameters["address"] ?: ""
                val pwHash = call.parameters["pwHash"] ?: error("password must be specified")
                val user = User(database.getNextUserId(),
                    name, email, phone, address, pwHash, User.Privilege.User)
                database.addUser(user)
                call.respond(HttpStatusCode.OK)
            }
            post("/login") {
                try {
                    val msg = call.receive<String>().drop(1).dropLast(1).split("|")
                    val user = database.getUserByEmail(msg[0])
                    if (user.password_hash == msg[1]) {
                        val token = JwtConfig.createAccessToken(user.id)
                        call.respond(token)
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