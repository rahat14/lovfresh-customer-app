package com.lovfreshuser.models

import com.google.gson.annotations.SerializedName

data class OrderHistoryResponse(
    @SerializedName("code")
    var code: Int? = 0, // 200
    @SerializedName("count")
    var count: Int? = 0, // 44
    @SerializedName("data")
    var `data`: OrderResults? = OrderResults(),
    @SerializedName("links")
    var links: Links? = Links(),
    @SerializedName("message")
    var message: String? = "", // Success
    @SerializedName("meta")
    var meta: Meta? = Meta()
)
