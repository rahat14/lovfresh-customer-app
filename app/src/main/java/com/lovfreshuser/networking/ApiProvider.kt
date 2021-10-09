package com.lovfreshuser.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiProvider {

    private fun retrofitService(): Retrofit {
        return Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiService.ROOT_URL)
            .client(getHttpClient())
            .build()
    }


    private fun getHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor() // loging the respose for testing purpose only
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.callTimeout(2, TimeUnit.MINUTES)
        httpClient.readTimeout(2, TimeUnit.MINUTES)
        httpClient.writeTimeout(2, TimeUnit.MINUTES)
        //adding the auth intercepter
       // httpClient.addInterceptor(AuthInterceptor())
        httpClient.addInterceptor(logging)
        return httpClient.build()
    }

    val dataApi: ApiService by lazy {
        retrofitService().create(ApiService::class.java)
    }
}