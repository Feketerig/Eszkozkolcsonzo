package hu.bme.aut.application.routing

import hu.bme.aut.application.backend.*
import hu.bme.aut.application.backend.utils.Success
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

//TODO all these error() calls should be checked

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
                    requireAccessLevel(User.Privilege.Handler) {
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

fun Application.leaseApi(leases: Leases) {
    routing {
        authenticate("basic-jwt") {
            route(ServerApiPath.leasePath) {
                put("/{id}") {
                    requireAccessLevel(User.Privilege.Handler) {
                        val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                        leases.deactivateLease(id)
                        call.respond(HttpStatusCode.OK)
                    }
                }
                post() {
                    requireAccessLevel(User.Privilege.Handler) {
                        val resId = call.parameters["resId"]?.toInt() ?: error("reservation id must be specicied")
                        val kiado = call.parameters["kiado"]?.toInt() ?: error("user id must be specicied")
                        val atvevo = call.parameters["atvevo"]?.toInt() ?: error("user id must be specicied")
                        leases.addLease(resId, kiado, atvevo)
                        call.respond(HttpStatusCode.OK)
                    }
                }
                delete("/{id}") {
                    requireAccessLevel(User.Privilege.Handler) {
                        val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                        when (leases.deleteLease(id)) {
                            is Success -> call.respond(HttpStatusCode.OK)
                            else -> call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                get() {
                    call.respond((leases.getActiveLeases() as Success<List<Lease>>).result)
                }
                get("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    when (val result = leases.getLease(id)) {
                        is Success -> call.respond(result.result)
                        else -> call.respond(HttpStatusCode.NotFound)
                    }
                }
                get("/reservation/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    when (val result = leases.getLeaseIdByReservationId(id)) {
                        is Success -> call.respond(result.result)
                        else -> call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}

fun Application.reservationApi(reservations: Reservations) {
    routing {
        authenticate("basic-jwt") {
            route(ServerApiPath.reservationPath) {
                get() {
                    call.respond((reservations.getAllReservations() as Success<List<Reservation>>).result)
                }
                get("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    when (val result = reservations.getReservation(id)) {
                        is Success -> call.respond(result.result)
                        else -> call.respond(HttpStatusCode.NotFound)
                    }
                }
                get("/user/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    when (val result = reservations.getAllReservationByUserId(id)) {
                        is Success -> call.respond(result.result)
                        else -> call.respond(HttpStatusCode.NotFound)
                    }
                }
                get("/device/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    when (val result = reservations.getReservationByDeviceId(id)) {
                        is Success -> call.respond(result.result)
                        else -> call.respond(HttpStatusCode.NotFound)
                    }
                }
                post() {
                    val deviceid = call.parameters["deviceid"]?.toInt() ?: error("device must be specified")
                    val userid = (call.authentication.principal as UserAuthPrincipal).id
                    val from = call.parameters["from"]?.toLong() ?: error("start date must be specified")
                    val to = call.parameters["to"]?.toLong() ?: error("end date must be specified")
                    reservations.addReservation(deviceid, from, to, userid)
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    when (val result = reservations.deleteReservation(id)) {
                        is Success -> call.respond(result.result)
                        else -> call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}

fun Application.userApi(users: Users) {
    routing {
        authenticate("basic-jwt") {
            route(ServerApiPath.userPath) {
                get() {
                    requireAccessLevel(User.Privilege.Handler) {
                        val email = call.request.queryParameters["email"] ?: error("Invalid username")
                        when (val result = users.getUserByEmail(email)) {
                            is Success -> call.respond(result.result)
                            else -> call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                get("/{id}") {
                    requireAccessLevel(User.Privilege.Handler) {
                        val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                        when (val result = users.getUserById(id)) {
                            is Success -> call.respond(result.result)
                            else -> call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                get("/{id}/name") {
                    requireAccessLevel(User.Privilege.Handler) {
                        val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                        when (val result = users.getUserNameById(id)) {
                            is Success -> call.respond(result.result)
                            else -> call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
            }
        }
        //Authentication not needed
        route(ServerApiPath.userPath) {
            post() {
                val name = call.parameters["name"] ?: error("user name must be specified")
                val email = call.parameters["email"] ?: error("user name must be specified")
                val phone = call.parameters["phone"] ?: error("user phone must be specified")
                val address = call.parameters["address"] ?: error("user address must be specified")
                val pwHash = call.parameters["pwHash"] ?: error("password must be specified")

                val emailAvailable: Boolean = (users.emailAlreadyExists(email) as Success).result.not()
                if (emailAvailable){
                    users.addUser(name, email, phone, address, pwHash, User.Privilege.User)
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Conflict)
                }
            }
            post("/login") {
                val msg = call.receive<String>().drop(1).dropLast(1).split("|")
                val result = users.getUserByEmail(msg[0])
                if (result is Success) {
                    val user = result.result
                    if (user.password_hash == msg[1]) {
                        val token = JwtConfig.createAccessToken(user.id, user.name, user.email, user.privilege.toString())
                        call.respond(token)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}