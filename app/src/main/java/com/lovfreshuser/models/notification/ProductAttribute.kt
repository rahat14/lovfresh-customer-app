package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName
import com.lovfreshuser.models.notification.Uom

data class ProductAttribute(
    @SerializedName("id")
    var id: Int? = 0, // 322
    @SerializedName("price")
    var price: String? = "", // 1.99
    @SerializedName("product")
    var product: Int? = 0, // 322
    @SerializedName("stock")
    var stock: String? = "", // 144.00
    @SerializedName("total_sell_product")
    var totalSellProduct: String? = "", // 10
    @SerializedName("uom")
    var uom: Uom? = Uom()
)