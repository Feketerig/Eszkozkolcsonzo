package hu.bme.aut.application.backend.utils

import hu.bme.aut.application.backend.Devices
import hu.bme.aut.application.backend.Leases
import hu.bme.aut.application.backend.Reservations
import hu.bme.aut.application.backend.Users
import hu.bme.aut.application.database.Database

fun getDeviceBackend(database: Database) = Devices(database)
fun getReservationBackend(database: Database) = Reservations(database)
fun getLeaseBackend(database: Database) = Leases(database)
fun getUserBackend(database: Database) = Users(database)