package com.lovfreshuser.networking

import com.google.gson.JsonObject
import com.lovfreshuser.models.BasicCategoryModel
import com.lovfreshuser.models.LoginResp
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    companion object {
        val ROOT_URL: String = "http://13.55.122.237/api/"
        val IMAGE_URL: String = "https://relax.spinnertechltd.com/storage/"
    }

    @GET("basic_categories")
    fun getBasicCategoryList():List<BasicCategoryModel>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") phone: String,
        @Field("password") password: String
    ): Call<LoginResp>


    @FormUrlEncoded
    @POST("signup")
    fun signUpUser(
        @Field("email") phone: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String,
        @Field("phone_number") phone_number: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String
    ): Call<JsonObject>

}