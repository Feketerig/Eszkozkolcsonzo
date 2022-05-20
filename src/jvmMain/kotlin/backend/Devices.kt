package backend

import database.Database
import database.WrongIdException
import model.Device

class Devices(private val database: Database) {

    suspend fun getAllDevices(): Result<List<Device>> {
        return Success(database.getAllDevices())
    }

    suspend fun getDevice(id: Int): Result<Device> {
        return try {
            Success(database.getDevice(id))
        } catch (e: WrongIdException) {
            Error(e)
        }
    }

    suspend fun addDevice(name: String, desc: String): Result<Unit> {
        database.addDevice(Device(database.getNextDeviceId(), name, desc, true))
        return Success(Unit)
    }

    suspend fun deleteDevice(id: Int): Result<Unit> {
        return try {
            database.deleteDevice(id)
            Success(Unit)
        } catch (e: WrongIdException) {
            Error(e)
        }
    }
}