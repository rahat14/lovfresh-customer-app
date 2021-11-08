package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("results")
    var results: MutableList<NotificationOrderItem>? = mutableListOf()
)