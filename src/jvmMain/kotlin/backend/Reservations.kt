package backend

import database.Database
import model.Reservation

class Reservations(private val database: Database) {

    suspend fun getAllReservations(): Result<List<Reservation>> {
        TODO()
    }

    suspend fun getReservation(id: Int): Result<Reservation> {
        TODO()
    }

    suspend fun getAllReservationByUserId(id: Int): Result<List<Reservation>> {
        TODO()
    }

    suspend fun getReservationByDeviceId(id: Int): Result<Reservation> {
        TODO()
    }

    suspend fun addReservation(reservation: Reservation): Result<Unit> {
        TODO()
    }

    suspend fun deleteReservation(id: Int): Result<Unit> {
        TODO()
    }
}