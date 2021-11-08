package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName

data class Uom(
    @SerializedName("formula")
    var formula: String? = "", // 1
    @SerializedName("id")
    var id: Int? = 0, // 4
    @SerializedName("name")
    var name: String? = "", // Each
    @SerializedName("value")
    var value: Double? = 0.0 // 1.0
)