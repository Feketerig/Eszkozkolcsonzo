package hu.bme.aut.application.backend

import hu.bme.aut.application.backend.utils.*
import hu.bme.aut.application.database.Database
import hu.bme.aut.application.database.WrongIdException
import model.Reservation
import model.User

class Reservations(private val database: Database) {

    suspend fun getAllReservations(): Result<List<Reservation>> {
        return Success(database.getAllReservations())
    }

    suspend fun getReservation(id: Int): Result<Reservation> {
        return try {
            Success(database.getReservation(id))
        } catch (e: WrongIdException) {
            NotFound(id)
        }
    }

    suspend fun getAllReservationByUserId(id: Int): Result<List<Reservation>> {
        return Success(database.getAllReservationByUserId(id))
    }

    suspend fun getReservationByDeviceId(id: Int): Result<Reservation> {
        return try {
            Success(database.getReservationByDeviceId(id))
        } catch (e: WrongIdException) {
            NotFound(id)
        }
    }

    suspend fun addReservation(deviceId: Int, from: Long, to: Long, userId: Int): Result<Unit> {
        return try {
            if (database.getDevice(deviceId).available) {
                database.setDeviceAvailability(deviceId, false)
                database.addReservation(Reservation(database.getNextReservationId(), deviceId, from, to, userId))
                Success(Unit)
            }
            else{
                Conflict("device $deviceId is already reserved")
            }
        } catch (e: WrongIdException) {
            NotFound(deviceId)
        }
    }

    suspend fun deleteReservation(reservationId: Int, userId: Int): Result<Unit> {
        return if (database.getActiveLeases().none { it.reservationId == reservationId }) {
            try {
                val reservation = database.getReservation(reservationId)
                if (reservation.userId == userId || database.getUserById(userId).privilege != User.Privilege.User) {
                    database.setDeviceAvailability(reservation.deviceId, true)
                    Success(database.deleteReservation(reservationId))
                }
                else {
                    Forbidden()
                }
            } catch (e: WrongIdException) {
                NotFound(reservationId)
            }
        }
        else {
            Conflict("reservation $reservationId is served, and active")
        }
    }
}