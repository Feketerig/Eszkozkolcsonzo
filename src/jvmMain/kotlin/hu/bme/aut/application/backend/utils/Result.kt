package hu.bme.aut.application.backend.utils

sealed class Result<out T : Any>
class Success<T : Any>(val result: T) : Result<T>()
class Error(val throwable: Throwable) : Result<Nothing>()
class Conflict(val parameterName: String = "") : Result<Nothing>()
class Unauthorized : Result<Nothing>()