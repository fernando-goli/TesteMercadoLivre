package com.example.testemercadolivre.core.common

import kotlinx.coroutines.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response

abstract class Repository {
    suspend fun <T> http(request: suspend () -> Response<T>): Result<T?> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = request()
                val body = response.body()
                if (response.errorBody() != null) {
                    throw IoException.ApiRequestError(
                        message = response.errorBody()?.string() ?: "Ocorreu um erro desconhecido.",
                        url = response.raw().request().url().toString(),
                        code = response.raw().code()
                    )
                }
                if (response.isSuccessful) {
                    Result.success(body)
                } else {
                    Result.error(code = response.code(), message = response.message())
                }
            } catch (e: Exception) {
                throw Exception(e)
            }
        }

    fun createJsonRequestBody(vararg params: Pair<String, Any?>): RequestBody =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString()
        )
}


