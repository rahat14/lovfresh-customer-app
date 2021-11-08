package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName
import com.lovfreshuser.models.Links
import com.lovfreshuser.models.Meta

data class NotificationResponse(
    @SerializedName("code")
    var code: Int? = 0, // 200
    @SerializedName("count")
    var count: Int? = 0, // 17
    @SerializedName("data")
    var `data`: Data? = Data(),
    @SerializedName("links")
    var links: Links? = Links(),
    @SerializedName("message")
    var message: String? = "", // Success
    @SerializedName("meta")
    var meta: Meta? = Meta()
)