package backend

import database.Database
import model.Lease

class Leases(private val database: Database) {

    suspend fun getActiveLeases(): Result<List<Lease>> {
        TODO()
    }

    suspend fun getLease(id: Int): Result<Lease> {
        TODO()
    }

    suspend fun addLease(lease: Lease): Result<Unit> {
        TODO()
    }

    suspend fun deleteLease(id: Int): Result<Unit> {
        TODO()
    }

    suspend fun activateLease(id: Int): Result<Unit> {
        TODO()
    }

    suspend fun deactivateLease(id: Int): Result<Unit> {
        TODO()
    }

    suspend fun getLeaseIdByReservationId(id: Int): Result<Int> {
        TODO()
    }
}