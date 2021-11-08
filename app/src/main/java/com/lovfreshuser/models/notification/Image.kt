package com.lovfreshuser.kotlin.data.remote.model.pagination.notification


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    var id: Int? = 0, // 365
    @SerializedName("image")
    var image: String? = ""
)