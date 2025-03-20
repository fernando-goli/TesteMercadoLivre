package com.example.testemercadolivre.search.data.datasource.remote.api

import com.example.testemercadolivre.search.data.models.AccessTokenModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("oauth/token")
    suspend fun getAccessToken(@Body params: RequestBody): Response<AccessTokenModel>

}