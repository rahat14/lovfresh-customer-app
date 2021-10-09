package com.lovfreshuser.networking

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        //  HelperClass.fetchAuthToken()?.let {
        requestBuilder.addHeader("Authorization", "Bearer ")
        //  }

        return chain.proceed(requestBuilder.build())
    }
}
