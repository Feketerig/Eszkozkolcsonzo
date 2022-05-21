package hu.bme.aut.application.backend

import hu.bme.aut.application.backend.utils.NotFound
import hu.bme.aut.application.backend.utils.Result
import hu.bme.aut.application.backend.utils.Success
import hu.bme.aut.application.database.Database
import hu.bme.aut.application.database.WrongIdException
import model.Lease

class Leases(private val database: Database) {

    suspend fun getActiveLeases(): Result<List<Lease>> {
        return Success(database.getActiveLeases())
    }

    suspend fun getLease(id: Int): Result<Lease> {
        return try {
            Success(database.getLease(id))
        } catch (e: WrongIdException) {
            NotFound(id)
        }
    }

    suspend fun addLease(reservationId: Int, kiado: Int, atvevo: Int): Result<Unit> {
        return Success(database.addLease(Lease(database.getNextLeaseId(), reservationId, kiado, atvevo, true)))
    }

    suspend fun deleteLease(id: Int): Result<Unit> {
        return try {
            Success(database.deleteLease(id))
        } catch (e: Exception) {
            NotFound(id)
        }
    }

    suspend fun activateLease(id: Int): Result<Unit> {
        return Success(database.activateLease(id))
    }

    suspend fun deactivateLease(id: Int): Result<Unit> {
        return Success(database.deactivateLease(id))
    }

    suspend fun getLeaseIdByReservationId(id: Int): Result<Int> {
        return try {
            Success(database.getLeaseIdByReservationId(id))
        } catch (e: WrongIdException) {
            NotFound(id)
        }
    }
}