package hu.bme.aut.application.routing

import backend.Devices
import backend.Success
import database.Database
import database.WrongIdException
import hu.bme.aut.application.routing.utils.requireAccessLevel
import hu.bme.aut.application.security.JwtConfig
import hu.bme.aut.application.security.UserAuthPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.Device
import model.Lease
import model.Reservation
import model.User
import utils.path.ServerApiPath

fun Application.deviceApi(devices: Devices){
    routing {
        authenticate("basic-jwt") {
            route(ServerApiPath.devicePath) {
                post() {
                    requireAccessLevel(User.Privilege.Handler) {
                        val name = call.parameters["name"] ?: error("device must have a name")
                        val desc = call.parameters["desc"] ?: ""
                        devices.addDevice(name, desc)
                        call.respond(HttpStatusCode.OK)
                    }
                }
                delete("/{id}") {
                    requireAccessLevel(User.Privilege.Handler){
                        val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                        when (devices.deleteDevice(id)) {
                            is Success -> call.respond(HttpStatusCode.OK)
                            else -> call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                get() {
                    call.respond((devices.getAllDevices() as Success<List<Device>>).result)
                }
                get("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    when (val result = devices.getDevice(id)) {
                        is Success -> call.respond(result.result)
                        else -> call.respond(HttpStatusCode.NotFound)
                    }

                }
            }
        }
    }
}

fun Application.leaseApi(database: Database) {
    routing {
        authenticate("req-handler-jwt") { // Handler privilege required
            route(ServerApiPath.leasePath) {
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
                authenticate("basic-jwt") { // Being a user is enough
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
                }
            }
        }
    }
}

fun Application.reservationApi(database: Database) {
    routing {
        authenticate("basic-jwt") {
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
                    val userid = (call.authentication.principal as UserAuthPrincipal).id
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

fun Application.userApi(database: Database) {  // Handler privilege required
    routing {
        authenticate("req-handler-jwt") {
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
                try {
                    val name = call.parameters["name"] ?: error("user name must be specified")
                    val email = call.parameters["email"] ?: error("user name must be specified")
                    val phone = call.parameters["phone"] ?: error("user phone must be specified")
                    val address = call.parameters["address"] ?: error("user address must be specified")
                    val pwHash = call.parameters["pwHash"] ?: error("password must be specified")
                    if (!database.emailAlreadyExists(email)){
                        val user = User(database.getNextUserId(), name, email, phone, address, pwHash, User.Privilege.User)
                        database.addUser(user)
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Conflict)
                    }
                } catch (e: IllegalStateException) {
                    call.respond(HttpStatusCode.PreconditionFailed)
                }
            }
            post("/login") {
                try {
                    val msg = call.receive<String>().drop(1).dropLast(1).split("|")
                    val user = database.getUserByEmail(msg[0])
                    if (user.password_hash == msg[1]) {
                        val token = JwtConfig.createAccessToken(user.id, user.name, user.email, user.privilege.toString())
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