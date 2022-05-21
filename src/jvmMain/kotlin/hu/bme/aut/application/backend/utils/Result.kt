package hu.bme.aut.application.backend.utils

sealed class Result<out T : Any>

class Success<T : Any>(val result: T) : Result<T>()
class Error(val throwable: Throwable) : Result<Nothing>()
class Conflict(val reason: String = "") : Result<Nothing>()
class Unauthorized : Result<Nothing>()
class Forbidden : Result<Nothing>()
class NotFound(val id: Int) : Result<Nothing>()