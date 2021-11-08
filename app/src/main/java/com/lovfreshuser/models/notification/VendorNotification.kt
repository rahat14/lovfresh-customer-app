package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName

data class VendorNotification(
    @SerializedName("id")
    var id: Int? = 0, // 114
    @SerializedName("is_sms")
    var isSms: Boolean? = false, // true
    @SerializedName("is_sound")
    var isSound: Boolean? = false, // true
    @SerializedName("is_vibration")
    var isVibration: Boolean? = false, // true
    @SerializedName("user")
    var user: Any? = Any(), // null
    @SerializedName("vendor")
    var vendor: Int? = 0 // 34
)