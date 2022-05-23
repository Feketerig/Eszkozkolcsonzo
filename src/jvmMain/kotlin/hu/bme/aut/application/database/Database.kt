package hu.bme.aut.application.database

import model.Device
import model.Lease
import model.Reservation
import model.User

interface Database {

    suspend fun getNextId(): Int
    suspend fun getNextDeviceId(): Int
    suspend fun getNextReservationId(): Int
    suspend fun getNextLeaseId(): Int
    suspend fun getNextUserId(): Int

    suspend fun getAllDevices(): List<Device>

    suspend fun getDevice(id: Int): Device

    suspend fun addDevice(device: Device)

    suspend fun deleteDevice(id: Int)

    suspend fun checkDeviceAvailability(deviceId: Int, startDate: Long, endDate: Long): Boolean

    suspend fun getActiveLeases(): List<Lease>

    suspend fun getLease(id: Int): Lease

    suspend fun addLease(lease: Lease)

    suspend fun deleteLease(id: Int)

    suspend fun activateLease(id: Int)

    suspend fun deactivateLease(id: Int)

    suspend fun getLeaseIdByReservationId(id: Int): Int

    suspend fun getAllReservations(): List<Reservation>

    suspend fun getReservation(id: Int): Reservation

    suspend fun getAllReservationByUserId(id: Int): List<Reservation>

    suspend fun getReservationsByDeviceId(id: Int): List<Reservation>

    suspend fun addReservation(reservation: Reservation)

    suspend fun deleteReservation(id: Int)

    suspend fun getUserByEmail(email: String): User

    suspend fun emailAlreadyExists(email: String): Boolean

    suspend fun addUser(user: User)

    suspend fun getUserNameById(userId: Int): String

    suspend fun getUserById(userId: Int): User
}