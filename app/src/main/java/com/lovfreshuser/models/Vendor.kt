package com.lovfreshuser.models


import com.google.gson.annotations.SerializedName

data class Vendor(
    @SerializedName("abn")
    var abn: String? = "", // 83634977423
    @SerializedName("acn")
    var acn: Any? = Any(), // null
    @SerializedName("customer")
    var customer: Any? = Any(), // null
    @SerializedName("delivery_type")
    var deliveryType: List<Any>? = listOf(),
    @SerializedName("description")
    var description: String? = "", // David Fresh Prestons
    @SerializedName("email")
    var email: String? = "", // davidsfresh2170@gmail.com
    @SerializedName("id")
    var id: Int? = 0, // 34
    @SerializedName("image_url")
    var imageUrl: String? = "", // /media/uploads/pictures/IMG-20210805-WA0007_RhGTT6K.jpg
    @SerializedName("is_updated")
    var isUpdated: Boolean? = false, // true
    @SerializedName("loyal")
    var loyal: String? = "", // 12
    @SerializedName("mobile")
    var mobile: String? = "", // +8801720527000
    @SerializedName("subscription")
    var subscription: Any? = Any(), // null
    @SerializedName("title")
    var title: String? = "", // David's Fresh Prestons
    @SerializedName("user")
    var user: User? = User(),
    @SerializedName("vendor_notification")
    var vendorNotification: List<VendorNotification>? = listOf(),
    @SerializedName("website")
    var website: String? = "" // www.facebook.com/davidsfreshprestons
)