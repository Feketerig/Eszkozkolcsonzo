package hu.bme.aut.application.backend

import hu.bme.aut.application.backend.utils.Conflict
import hu.bme.aut.application.backend.utils.NotFound
import hu.bme.aut.application.backend.utils.Result
import hu.bme.aut.application.backend.utils.Success
import hu.bme.aut.application.database.Database
import hu.bme.aut.application.database.WrongIdException
import model.Device

class Devices(private val database: Database) {

    suspend fun getAllDevices(): Result<List<Device>> {
        return Success(database.getAllDevices())
    }

    suspend fun getDevice(id: Int): Result<Device> {
        return try {
            Success(database.getDevice(id))
        } catch (e: WrongIdException) {
            NotFound(id)
        }
    }

    suspend fun addDevice(name: String, desc: String): Result<Unit> {
        return Success(database.addDevice(Device(database.getNextDeviceId(), name, desc)))
    }

    suspend fun deleteDevice(id: Int): Result<Unit> {
        return try {
            if (database.getReservationsByDeviceId(id).isEmpty())
                Success(database.deleteDevice(id))
            else
                Conflict("device is reserved, cannot be deleted")
        } catch (e: Exception) {
            NotFound(id)
        }
    }
}