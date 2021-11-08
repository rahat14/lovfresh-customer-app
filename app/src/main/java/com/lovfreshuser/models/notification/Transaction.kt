package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("id")
    var id: Int? = 0, // 91
    @SerializedName("invoice")
    var invoice: String? = "", // http://13.55.122.237/media/uploads/invoices/order_1631747493.592755.pdf
    @SerializedName("transactionsid")
    var transactionsid: String? = "" // txn_3Ja7G9G5dbidyhlP19YqrImn
)