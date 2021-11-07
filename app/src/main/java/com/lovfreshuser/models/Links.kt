package com.lovfreshuser.models


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("first")
    var first: String? = "", // http://13.55.122.237/api/notification-list?page=1&page_size=10
    @SerializedName("last")
    var last: String? = "", // http://13.55.122.237/api/notification-list?page=2&page_size=10
    @SerializedName("next")
    var next: String? = "", // http://13.55.122.237/api/notification-list?page=2&page_size=10
    @SerializedName("previous")
    var previous: Any? = Any() // null
)