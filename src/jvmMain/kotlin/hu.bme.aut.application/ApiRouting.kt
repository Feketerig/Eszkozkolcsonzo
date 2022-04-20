package hu.bme.aut.application

import utils.path.ServerApiPath
import database.Database
import database.WrongIdException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.deviceApi(database: Database){
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

fun Routing.leaseApi(database: Database){
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

fun Routing.reservationApi(database: Database){
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