package hu.bme.aut.application.backend

import hu.bme.aut.application.backend.utils.NotFound
import hu.bme.aut.application.backend.utils.Result
import hu.bme.aut.application.backend.utils.Success
import hu.bme.aut.application.database.Database
import hu.bme.aut.application.database.WrongIdException
import model.Reservation

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
        return Success(database.addReservation(Reservation(database.getNextReservationId(), deviceId, from, to, userId)))
    }

    suspend fun deleteReservation(id: Int): Result<Unit> {
        return try {
            Success(database.deleteReservation(id))
        } catch (e: Exception) {
            NotFound(id)
        }
    }
}