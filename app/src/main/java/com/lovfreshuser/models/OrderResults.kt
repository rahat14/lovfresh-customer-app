package com.lovfreshuser.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrderResults(
    @SerializedName("results")
    var results: MutableList<OrderHistoryItem>? = mutableListOf()
):Serializable