package com.lovfreshuser.models

import com.google.gson.annotations.SerializedName

data class OrderResults(
    @SerializedName("results")
    var results: MutableList<OrderHistoryItem>? = mutableListOf()
)