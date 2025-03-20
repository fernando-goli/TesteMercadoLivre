package com.example.testemercadolivre.core.common

sealed class Exceptions(val code: String, message: String = "", val messageRes: Int = -1) : Exception(message) {
    class ApiUnreachable : Exceptions("IO_ERROR_0001", "Não foi possível se comunicar com o servidor.")
    class ApiRequestError(message: String, code: Int) : Exceptions(code.toString(), message)
}

fun isPageNotFound(code: Int) = code == 404