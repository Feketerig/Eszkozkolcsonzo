package hu.bme.aut.application.backend

import hu.bme.aut.application.backend.utils.Error
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
            Error(e)
        }
    }

    suspend fun addLease(reservationId: Int, kiado: Int, atvevo: Int): Result<Unit> {
        database.addLease(Lease(database.getNextLeaseId(), reservationId, kiado, atvevo, true))
        return Success(Unit)
    }

    suspend fun deleteLease(id: Int): Result<Unit> {
        return try {
            database.deleteLease(id)
            Success(Unit)
        } catch (e: WrongIdException) {
            Error(e)
        }
    }

    suspend fun activateLease(id: Int): Result<Unit> {
        database.activateLease(id)
        return Success(Unit)
    }

    suspend fun deactivateLease(id: Int): Result<Unit> {
        database.deactivateLease(id)
        return Success(Unit)
    }

    suspend fun getLeaseIdByReservationId(id: Int): Result<Int> {
        return try {
            Success(database.getLeaseIdByReservationId(id))
        } catch (e: WrongIdException) {
            Error(e)
        }
    }
}