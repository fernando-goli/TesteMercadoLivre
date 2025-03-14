package com.example.testemercadolivre.core.common

data class Result<out T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null,
    val code: Int? = null,
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
    }

    companion object {
        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data)
        }

        fun <T> error(
            message: String? = null,
            data: T? = null,
            code: Int? = null,
        ): Result<T> {
            return Result(Status.ERROR, data, message, code)
        }

        fun <T> loading(): Result<T> {
            return Result(Status.LOADING)
        }
    }
}