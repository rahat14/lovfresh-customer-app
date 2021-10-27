package com.lovfreshuser.networking

import com.lovfreshuser.utils.SharedPrefManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        //  HelperClass.fetchAuthToken()?.let {
        requestBuilder.addHeader(
            "Authorization",
            "Token 9f7da468bac09005b29708a097a99d3648b79e2e321b1ec23ba100041c2eb1b4"
        )
        requestBuilder.addHeader("vendor-id", "${SharedPrefManager.getVendorID()}")
        //  }

        return chain.proceed(requestBuilder.build())
    }
}
