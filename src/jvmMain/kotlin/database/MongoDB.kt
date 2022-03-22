package database

import model.Device
import model.Lease
import model.Reservation
import model.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoDB(
    private val database: CoroutineDatabase
) : Database {
    private val devices = database.getCollection<Device>()
    private val leases = database.getCollection<Lease>()
    private val reservations = database.getCollection<Reservation>()
    private val users = database.getCollection<User>()

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

    override suspend fun getAllReservations(): List<Reservation> = reservations.find().toList()

    override suspend fun getReservation(id: Int): Reservation =
        reservations.find(Reservation::id eq id).first() ?: throw WrongIdException()

    override suspend fun addReservation(reservation: Reservation) {
        reservations.insertOne(reservation)
    }

    override suspend fun deleteReservation(id: Int) {
        reservations.deleteOne(Reservation::id eq id)
    }
}