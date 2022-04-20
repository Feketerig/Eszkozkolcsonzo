package model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val phone: String,
    val address: String,
    val email: String,
    val privilege: Privilege
){
    @Serializable
    enum class Privilege{
        Admin, User
    }
}
