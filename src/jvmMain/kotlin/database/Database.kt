package database

import model.Device
import model.Lease
import model.Reservation

interface Database {

    suspend fun getAllDevices(): List<Device>

    suspend fun getDevice(id: Int): Device

    suspend fun addDevice(device: Device)

    suspend fun deleteDevice(id: Int)

    suspend fun getActiveLeases(): List<Lease>

    suspend fun getLease(id: Int): Lease

    suspend fun addLease(lease: Lease)

    suspend fun deleteLease(id: Int)

    suspend fun activateLease(id: Int)

    suspend fun deactivateLease(id: Int)

    suspend fun getAllReservations(): List<Reservation>

    suspend fun getReservation(id: Int): Reservation

    suspend fun addReservation(reservation: Reservation)

    suspend fun deleteReservation(id: Int)
}