package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName

data class OrderProduct(
    @SerializedName("id")
    var id: Int? = 0, // 117
    @SerializedName("order")
    var order: Int? = 0, // 94
    @SerializedName("price")
    var price: String? = "", // 1.99
    @SerializedName("product")
    var product: Product? = Product(),
    @SerializedName("quantity")
    var quantity: Int? = 0, // 1
    @SerializedName("total_price")
    var totalPrice: Any? = Any(), // null
    @SerializedName("uom")
    var uom: Int? = 0 // 4
)