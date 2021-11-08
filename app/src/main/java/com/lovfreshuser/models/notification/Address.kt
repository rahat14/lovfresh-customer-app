package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("address")
    var address: String? = "", // d2a Wroxham St, Prestons NSW 2170, Australia Prestons New South Wales 2170
    @SerializedName("address_type")
    var addressType: Int? = 0, // 1
    @SerializedName("flat_number")
    var flatNumber: String? = "",
    @SerializedName("full_name")
    var fullName: String? = "", // Dhaka
    @SerializedName("id")
    var id: Int? = 0, // 146
    @SerializedName("is_active")
    var isActive: Boolean? = false, // true
    @SerializedName("is_selected")
    var isSelected: Boolean? = false, // true
    @SerializedName("landmark")
    var landmark: String? = "",
    @SerializedName("lat_long")
    var latLong: String? = "", // SRID=4326;POINT (-33.9496694 150.8637415)
    @SerializedName("latitude")
    var latitude: String? = "", // -33.9496694
    @SerializedName("longitude")
    var longitude: String? = "", // 150.8637415
    @SerializedName("mobile")
    var mobile: String? = "", // 123456789
    @SerializedName("streat")
    var streat: String? = "" // d2a Wroxham St, Prestons NSW 2170, Australia
)