package com.lovfreshuser.networking

import com.lovfreshuser.models.LoginResp
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    companion object {
        val ROOT_URL: String = "http://13.55.122.237/api/"
        val IMAGE_URL: String = "https://relax.spinnertechltd.com/storage/"
    }

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") phone: String,
        @Field("password") password: String
    ): Call<LoginResp>

}