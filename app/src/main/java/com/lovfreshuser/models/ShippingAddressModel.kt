package com.lovfreshuser.models

data class ShippingAddressModel(
    val address: String,
    val address_type: Int,
    val email: String,
    val flat_number: String,
    val full_name: String,
    val id: Int,
    val is_active: Boolean,
    val is_selected: Boolean,
    val landmark: String,
    val lat_long: String,
    val latitude: String,
    val longitude: String,
    val mobile: String,
    val streat: String,
    var SelectedAddress: Boolean = false
)