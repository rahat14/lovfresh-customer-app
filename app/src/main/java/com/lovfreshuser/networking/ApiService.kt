package com.lovfreshuser.networking

import com.google.gson.JsonObject
import com.lovfreshuser.models.*
import com.lovfreshuser.models.notification.NotificationResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        val ROOT_URL: String = "http://13.55.122.237/api/"
        val IMAGE_URL: String = "http://13.55.122.237"
    }


    @GET("products")
    fun searchProducts(
        @Query("vendor") vendor_id: String,
        @Query("title") title: String,
        @Query("page_size") page_size: Int,
        @Query("page") page: Int
    ): Call<ProductListResponse>

    @GET("address")
    fun getAddress(): Call<List<ShippingAddressModel>>


    @Headers("Content-Type: application/json")
    @POST("address/")
    fun createAddress(
        @Body params: RequestBody
    ): Call<JsonObject>

    @GET("notification-list")
    fun getNotifications(
        @Query("page_size") page_size: Int,
        @Query("page") page: Int
    ): Call<NotificationResponse>


    // store information
    @GET("v2/vendor/search-list")
    fun getVendorDetails(
        @Query("id") vendor_id: String,
    ): Call<List<Vendor>>

    //single order deitals

    @GET("orders/{order_id}")
    fun getOrderDetails(
        @Path("order_id") order_id: Int,
    ): Call<OrderHistoryItem>

    //orv order
    @GET("v2/orders")
    fun getPrevOrders(
        @Query("page_size") page_size: Int,
        @Query("page") page: Int,
        @Query("status") status: String
    ): Call<OrderHistoryResponse>

    // v2/vendor/slot-setting/?vendor_id=
    @GET("v2/vendor/slot-setting")
    fun getTimeSlots(
        @Query("vendor_id") vendor_id: String,
        @Query("delivery_type") delivery_type: String

    ): Call<List<DateModel>>

    //serch vendor
    ///search?latitude=-33.8761815&longitude=151.2463338
    @GET("vendors/search")
    fun searchVendor(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
    ): Call<List<VendorItem>>

    @GET("products")
    fun fetchProductList(
        @Query("vendor") vendor_id: Int,
        @Query("categories") categories: Int,
        @Query("page_size") page_size: Int,
        @Query("page") page: Int,
        @Query("type") type: String = ""
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
    @POST("send-otp")
    fun sendOtp(
        @Field("phone_number") phone_number: String,
        @Field("otp") otp: String
    ): Call<JsonObject>


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