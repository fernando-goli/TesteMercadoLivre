package com.example.testemercadolivre.search.data.repositories

import com.example.testemercadolivre.BuildConfig
import com.example.testemercadolivre.core.common.RemoteApiVerifier
import com.example.testemercadolivre.core.common.Repository
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.core.common.fold
import com.example.testemercadolivre.search.data.datasource.remote.api.AuthService
import javax.inject.Inject

interface AuthRepository {
    suspend fun getAccessToken(code: String): Result<String>
    suspend fun getRefreshToken(refreshToken: String): Result<String>
}

class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
) : Repository(RemoteApiVerifier), AuthRepository {

    override suspend fun getAccessToken(code: String): Result<String> {
        val accessToken = http {
            service.getAccessToken(
                createJsonRequestBody(
                    "grant_type" to "authorization_code",
                    "client_id" to BuildConfig.CLIENT_ID,
                    "client_secret" to BuildConfig.CLIENT_SECRET,
                    "code" to code,
                    "redirect_uri" to "https://www.google.com/"
                )
            )
        }
        accessToken.fold(
            onSuccess = { return Result.Success(it.refresh_token) },
            onError = { codeError, message -> return Result.Error(codeError, message) },
            onException = { return Result.Exception(it) }
        )

    }

    override suspend fun getRefreshToken(refreshToken: String): Result<String> {
        val accessToken = http {
            service.getAccessToken(
                createJsonRequestBody(
                    "grant_type" to "refresh_token",
                    "client_id" to BuildConfig.CLIENT_ID,
                    "client_secret" to BuildConfig.CLIENT_SECRET,
                    "refresh_token" to refreshToken
                )
            )
        }
        accessToken.fold(
            onSuccess = { return Result.Success(it.access_token) },
            onError = { code, message -> return Result.Error(code, message) },
            onException = { return Result.Exception(it) }
        )
    }
}