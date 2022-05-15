package utils.path

object ServerApiPath {
    private const val apiPath: String = "/api"

    val devicePath: String
        get() = "$apiPath/devices"

    val leasePath: String
        get() = "$apiPath/leases"

    val reservationPath: String
        get() = "$apiPath/reservations"

    val userPath: String
        get() = "$apiPath/users"
}