package model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val privilege: Privilege,

    val password_hash: String,
    val auth_token: String,
){
    @Serializable
    enum class Privilege{
        Admin, User
    }
}
