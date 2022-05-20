package backend

import database.Database

fun getDeviceBackend(database: Database) = Devices(database)
fun getReservationBackend(database: Database) = Reservations(database)
fun getLeaseBackend(database: Database) = Leases(database)
fun getUserBackend(database: Database) = Users(database)