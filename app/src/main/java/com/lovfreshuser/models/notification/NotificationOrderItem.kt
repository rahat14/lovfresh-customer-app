package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName

data class NotificationOrderItem(
    @SerializedName("address")
    var address: Address? = Address(),
    @SerializedName("coupon")
    var coupon: Any? = Any(), // null
    @SerializedName("created_at")
    var createdAt: String? = "", // 2021-09-15T23:11:22.895422Z
    @SerializedName("deliver_date")
    var deliverDate: String? = "", // 2021-09-19
    @SerializedName("delivery_cost")
    var deliveryCost: String? = "", // 2.99
    @SerializedName("end_time")
    var endTime: String? = "", // 14:00:00
    @SerializedName("get_status")
    var getStatus: String? = "", // Waiting to Accepted
    @SerializedName("id")
    var id: Int? = 0, // 94
    @SerializedName("is_notification")
    var isNotification: Boolean? = false, // false
    @SerializedName("is_vendor_notification")
    var isVendorNotification: Boolean? = false, // false
    @SerializedName("order_number")
    var orderNumber: String? = "", // 764754
    @SerializedName("order_products")
    var orderProducts: List<OrderProduct>? = listOf(),
    @SerializedName("order_type")
    var orderType: String? = "", // pickup
    @SerializedName("payment_type")
    var paymentType: String? = "", // card
    @SerializedName("rejection_reason")
    var rejectionReason: Any? = Any(), // null
    @SerializedName("start_time")
    var startTime: String? = "", // 12:00:00
    @SerializedName("status")
    var status: String? = "", // created
    @SerializedName("total")
    var total: String? = "", // 4.98
    @SerializedName("transaction")
    var transaction: List<Transaction>? = listOf(),
    @SerializedName("user")
    var user: User? = User(),
    @SerializedName("vendor")
    var vendor: Vendor? = Vendor()
)