package com.example.testemercadolivre.core.common

import com.example.testemercadolivre.BuildConfig
import kotlinx.coroutines.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import org.jsoup.Jsoup
import timber.log.Timber

interface ApiVerifier {
    fun isApiAvailable(): Boolean
    suspend fun isApiAvailableToken(token: String): Boolean
}

object RemoteApiVerifier : ApiVerifier {
    override fun isApiAvailable(): Boolean {
        return try {
            val response = Jsoup.connect("${BuildConfig.BASE_URL}items/MLB3983111753/description")
                .ignoreContentType(true).execute()
            response.statusCode() == 200
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isApiAvailableToken(token: String): Boolean = withContext(Dispatchers.IO) {
        val map = mapOf("Authorization" to "Bearer $token")
        return@withContext try {
            val response = Jsoup.connect("${BuildConfig.BASE_URL}items/MLB3983111753")
                .ignoreContentType(true).headers(map).execute()
            response.statusCode() == 200
        } catch (e: Exception) {
            false
        }
    }
}

abstract class Repository(private val apiVerifier: RemoteApiVerifier) {

    private suspend fun <T> withApi(online: suspend () -> T): T = withContext(Dispatchers.IO) {
        when {
            apiVerifier.isApiAvailable() -> online()
            else -> throw Exceptions.ApiUnreachable()
        }
    }

    suspend fun <T : Any> http(request: suspend () -> Response<T>): Result<T> = withApi {
        return@withApi try {
            val response = request()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                Timber.d(response.message())
                Result.Error(
                    code = response.code(),
                    message = response.message() ?: "Ocorreu um erro desconhecido."
                )
            }
        } catch (e: Exception) {
            Timber.d(e)
            Result.Exception(e)
        }
    }

    fun createJsonRequestBody(vararg params: Pair<String, Any?>): RequestBody =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString()
        )
}


