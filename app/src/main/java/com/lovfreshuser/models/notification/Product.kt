package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName
import com.lovfreshuser.kotlin.data.remote.model.pagination.notification.Image

data class Product(
    @SerializedName("categories")
    var categories: List<Int>? = listOf(),
    @SerializedName("created_at")
    var createdAt: String? = "", // 2021-09-03T15:53:20.734259Z
    @SerializedName("end_date_time")
    var endDateTime: Any? = Any(), // null
    @SerializedName("id")
    var id: Int? = 0, // 322
    @SerializedName("images")
    var images: List<Image>? = listOf(),
    @SerializedName("is_active")
    var isActive: Boolean? = false, // true
    @SerializedName("is_hide")
    var isHide: Boolean? = false, // false
    @SerializedName("long_description")
    var longDescription: String? = "",
    @SerializedName("price")
    var price: String? = "", // 1.99
    @SerializedName("product_attributes")
    var productAttributes: List<ProductAttribute>? = listOf(),
    @SerializedName("promotional")
    var promotional: Boolean? = false, // false
    @SerializedName("promotional_price")
    var promotionalPrice: Any? = Any(), // null
    @SerializedName("quantity")
    var quantity: String? = "", // 1.00
    @SerializedName("short_description")
    var shortDescription: String? = "", // Produce of Australia
    @SerializedName("start_date_time")
    var startDateTime: Any? = Any(), // null
    @SerializedName("title")
    var title: String? = "", // Cauliflower Half
    @SerializedName("type")
    var type: String? = "", // SHOPONLINE
    @SerializedName("updated_at")
    var updatedAt: String? = "", // 2021-09-06T12:37:17.110205Z
    @SerializedName("vendor")
    var vendor: Any? = Any() // null
)