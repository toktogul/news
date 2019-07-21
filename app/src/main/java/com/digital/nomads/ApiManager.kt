package com.digital.nomads

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap


class ApiManager {

    companion object {
        val API_KEY = "65ad666cf4194c8f8441fa86a0ef58b8"

        fun createApi(): NewsApi {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()


            return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
        }
    }
}


interface NewsApi {

    @GET("v2/everything")
    fun getNews(@QueryMap map: Map<String, @JvmSuppressWildcards Any>): Call<NewsResponse>
}