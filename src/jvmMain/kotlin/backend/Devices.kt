package backend

import database.Database
import model.Device

class Devices(private val database: Database) {

    suspend fun getAllDevices(): Result<List<Device>> {
        TODO()
    }

    suspend fun getDevice(id: Int): Result<Device> {
        TODO()
    }

    suspend fun addDevice(name: String, desc: String): Result<Unit> {
        database.addDevice(Device(database.getNextDeviceId(), name, desc, true))
        return Success(Unit)
    }

    suspend fun deleteDevice(id: Int): Result<Unit> {
        TODO()
    }
}