package com.example.testemercadolivre.search.di

import com.example.testemercadolivre.BuildConfig
import com.example.testemercadolivre.core.util.API_DATE_FORMAT
import com.example.testemercadolivre.search.data.datasource.remote.api.AuthService
import com.example.testemercadolivre.search.data.datasource.remote.api.SearchService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val gson = GsonBuilder().setDateFormat(API_DATE_FORMAT).create()

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: LoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

    @Singleton
    @Provides
    @SearchRetrofit
    fun providesSearchRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesSearchService(@SearchRetrofit retrofit: Retrofit): SearchService =
        retrofit.create()

    @Singleton
    @Provides
    @AuthRetrofit
    fun providesAuthRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesAuthService(@AuthRetrofit retrofit: Retrofit): AuthService =
        retrofit.create()

}

class LoggingInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val url = request
            .url()
            .newBuilder()
            .build()

        val requestBuilder = request.newBuilder()
        requestBuilder.url(url)

        requestBuilder.addHeader("Accept", "application/json")
        return chain.proceed(requestBuilder.build())
    }
}