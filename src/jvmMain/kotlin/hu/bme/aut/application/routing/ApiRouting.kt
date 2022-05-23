package hu.bme.aut.application.routing

import hu.bme.aut.application.backend.Devices
import hu.bme.aut.application.backend.Leases
import hu.bme.aut.application.backend.Reservations
import hu.bme.aut.application.backend.Users
import hu.bme.aut.application.backend.utils.*
import hu.bme.aut.application.routing.utils.requireAccessLevel
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
                            is Conflict -> call.respond(HttpStatusCode.Conflict)
                            is NotFound -> call.respond(HttpStatusCode.NotFound)
                            else -> call.respond(HttpStatusCode.InternalServerError)
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
                        is NotFound -> call.respond(HttpStatusCode.NotFound)
                        else -> call.respond(HttpStatusCode.InternalServerError)
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
                            is NotFound -> call.respond(HttpStatusCode.NotFound)
                            else -> call.respond(HttpStatusCode.InternalServerError)
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
                        is NotFound -> call.respond(HttpStatusCode.NotFound)
                        else -> call.respond(HttpStatusCode.InternalServerError)
                    }
                }
                get("/reservation/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                    when (val result = leases.getLeaseIdByReservationId(id)) {
                        is Success -> call.respond(result.result)
                        is NotFound -> call.respond(HttpStatusCode.NotFound)
                        else -> call.respond(HttpStatusCode.InternalServerError)
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
                    requireAccessLevel(User.Privilege.Handler) {
                        call.respond((reservations.getAllReservations() as Success<List<Reservation>>).result)
                    }
                }
                get("/{id}") {
                    requireAccessLevel(User.Privilege.Handler) {
                        val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                        when (val result = reservations.getReservation(id)) {
                            is Success -> call.respond(result.result)
                            is NotFound -> call.respond(HttpStatusCode.NotFound)
                            else -> call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                }
                get("/device/{id}") {
                    requireAccessLevel(User.Privilege.Handler) {
                        val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                        when (val result = reservations.getReservationsByDeviceId(id)) {
                            is Success -> call.respond(result.result)
                            is NotFound -> call.respond(HttpStatusCode.NotFound)
                            else -> call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                }
                get("/user") {
                    val id = (call.authentication.principal as UserAuthPrincipal).id
                    when (val result = reservations.getAllReservationByUserId(id)) {
                        is Success -> call.respond(result.result)
                        is NotFound -> call.respond(HttpStatusCode.NotFound)
                        else -> call.respond(HttpStatusCode.InternalServerError)
                    }
                }
                post() {
                    val deviceid = call.parameters["deviceid"]?.toInt() ?: error("device must be specified")
                    val userid = (call.authentication.principal as UserAuthPrincipal).id
                    val from = call.parameters["from"]?.toLong() ?: error("start date must be specified")
                    val to = call.parameters["to"]?.toLong() ?: error("end date must be specified")
                    when (val result = reservations.addReservation(deviceid, from, to, userid)) {
                        is Success -> call.respond(HttpStatusCode.OK)
                        is Conflict -> call.respond(HttpStatusCode.Conflict, result.reason)
                        is NotFound -> call.respond(HttpStatusCode.NotFound, result.id)
                        else -> call.respond(HttpStatusCode.InternalServerError)
                    }
                }
                delete("/{id}") {
                    val userId = (call.authentication.principal as UserAuthPrincipal).id
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    when (val result = reservations.deleteReservation(id, userId)) {
                        is Success -> call.respond(result.result)
                        is NotFound -> call.respond(HttpStatusCode.NotFound, result.id)
                        is Forbidden -> call.respond(HttpStatusCode.Forbidden)
                        else -> call.respond(HttpStatusCode.InternalServerError)
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
                            is NotFound -> call.respond(HttpStatusCode.NotFound)
                            else -> call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                }
                get("/{id}") {
                    requireAccessLevel(User.Privilege.Handler) {
                        val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                        when (val result = users.getUserById(id)) {
                            is Success -> call.respond(result.result)
                            is NotFound -> call.respond(HttpStatusCode.NotFound)
                            else -> call.respond(HttpStatusCode.InternalServerError)
                        }
                    }
                }
                get("/{id}/name") {
                    requireAccessLevel(User.Privilege.Handler) {
                        val id = call.parameters["id"]?.toInt() ?: error("Invalid id")
                        when (val result = users.getUserNameById(id)) {
                            is Success -> call.respond(result.result)
                            is NotFound -> call.respond(HttpStatusCode.NotFound)
                            else -> call.respond(HttpStatusCode.InternalServerError)
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
                when (val result = users.registerUser(name, email, phone, address, pwHash)) {
                    is Success -> call.respond(HttpStatusCode.OK)
                    is Conflict -> call.respond(HttpStatusCode.Conflict, result.reason)
                    is PreconditionFailed -> call.respond(HttpStatusCode.PreconditionFailed, result.reason)
                    else -> call.respond(HttpStatusCode.InternalServerError)
                }
            }
            post("/login") {
                val msg = call.receive<String>().split("|")
                when (val result = users.loginWith(msg[0], msg[1])) {
                    is Success -> call.respond(result.result)
                    is Unauthorized -> call.respond(HttpStatusCode.Unauthorized)
                    is NotFound -> call.respond(HttpStatusCode.NotFound)
                    else -> call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}