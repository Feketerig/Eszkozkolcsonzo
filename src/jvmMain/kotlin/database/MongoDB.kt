package database

import model.Device
import model.Lease
import model.Reservation
import model.User
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoDB(
    database: CoroutineDatabase
) : Database {
    private val devices = database.getCollection<Device>()
    private val leases = database.getCollection<Lease>()
    private val reservations = database.getCollection<Reservation>()
    private val users = database.getCollection<User>()
    private val ids = database.getCollection<IdPair>()

    override suspend fun getNextGlobalId(): Int {
        val nextID = (ids.findOne(IdPair::name eq IdPair.global_id)?.id ?: 0) + 1
        ids.updateOne(IdPair::name eq IdPair.global_id, setValue(IdPair::id, nextID), upsert())
        return nextID
    }

    override suspend fun getAllDevices(): List<Device> = devices.find().toList()

    override suspend fun getDevice(id: Int): Device =
        devices.find(Device::id eq id).first() ?: throw WrongIdException()

    override suspend fun addDevice(device: Device) {
        devices.insertOne(device)
    }

    override suspend fun deleteDevice(id: Int) {
        devices.deleteOne(Device::id eq id)
    }

    override suspend fun getActiveLeases(): List<Lease> = leases.find(Lease::active eq true).toList()

    override suspend fun getLease(id: Int): Lease = leases.find(Lease::id eq id).first() ?: throw WrongIdException()

    override suspend fun addLease(lease: Lease) {
        leases.insertOne(lease)
    }

    override suspend fun deleteLease(id: Int) {
        leases.deleteOne(Lease::id eq id)
    }

    override suspend fun activateLease(id: Int) {
        leases.updateOne(Lease::id eq id, Lease::active eq true)
    }

    override suspend fun deactivateLease(id: Int) {
        leases.updateOne(Lease::id eq id, Lease::active eq false)
    }

    override suspend fun getLeaseIdByReservationId(id: Int): Int {
        return leases.findOne(Lease::reservation / Reservation::id eq id)?.id ?: 1
    }

    override suspend fun getAllReservations(): List<Reservation> = reservations.find().toList()

    override suspend fun getReservation(id: Int): Reservation =
        reservations.find(Reservation::id eq id).first() ?: throw WrongIdException()

    override suspend fun getAllReservationByUserId(id: Int): List<Reservation> {
        return reservations.find(Reservation::userId eq id).toList()
    }

    override suspend fun getReservationByDeviceId(id: Int): Reservation {
        return reservations.findOne(Reservation::deviceId eq id) ?: throw WrongIdException()
    }

    override suspend fun addReservation(reservation: Reservation) {
        reservations.insertOne(reservation)
    }

    override suspend fun deleteReservation(id: Int) {
        reservations.deleteOne(Reservation::id eq id)
    }

    override suspend fun getUserByEmail(email: String): User {
        return users.findOne(User::email eq email) ?: throw WrongIdException()
    }

    override suspend fun addUser(user: User) {
        users.insertOne(user)
    }

    override suspend fun getUserNameById(userId: Int): String {
        return users.findOneById(userId)?.name ?: throw WrongIdException()
    }

    override suspend fun getUserById(userId: Int): User {
        return users.findOneById(userId) ?: throw WrongIdException()
    }

    @kotlinx.serialization.Serializable
    private data class IdPair(val name: String, val id: Int){
        companion object {
            val global_id = "GLOBAL"
        }
    }
}

