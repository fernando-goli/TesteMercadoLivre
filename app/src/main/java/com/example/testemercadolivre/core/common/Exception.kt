package com.example.testemercadolivre.core.common

sealed class IoException(val code: String, message: String = "", val messageRes: Int = -1) : Exception(message) {
    class ApiUnreachable : IoException("IO_ERROR_0001", "Não foi possível se comunicar com o servidor.")
    class ApiRequestError(message: String, url: String, code: Int) : IoException(code.toString(), message)
}