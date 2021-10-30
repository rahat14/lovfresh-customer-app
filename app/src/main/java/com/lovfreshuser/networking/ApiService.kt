package com.lovfreshuser.networking

import com.google.gson.JsonObject
import com.lovfreshuser.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        val ROOT_URL: String = "http://13.55.122.237/api/"
        val IMAGE_URL: String = "http://13.55.122.237"
    }


    //api/products/?vendor=34&categories=131&page_size=3


    @GET("products")
    fun fetchProductList(
        @Query("vendor") vendor_id: Int,
        @Query("categories") categories: Int,
        @Query("page_size") page_size: Int,
        @Query("page") page: Int
    ): Call<ProductListResponse>

    @GET("shop-online")
    fun getShopOnlineCategoryOnTab(
        @Query("vendor") id: String
    ): Call<List<ProductCategory>>

    @GET("basic_categories/{id}")
    fun getProductCategoryOnTab(
        @Path("id") id: String
    ): Call<Vendor_Product_Category>

    @GET("basic_categories")
    fun getBasicCategoryList(): Call<List<BasicCategoryModel>>

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