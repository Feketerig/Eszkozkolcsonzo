package api

class ServerApi {

    companion object {
        const val apiPath: String = "/api"

        val devicePath: String
            get() {return "$apiPath/devices" }

        val leasePath: String
            get() {return "$apiPath/leases" }

        val reservationPath: String
            get() {return "$apiPath/reservations" }

        val userPath: String
            get() {return "$apiPath/users" }
    }
}