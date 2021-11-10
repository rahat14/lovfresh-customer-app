package com.lovfreshuser.models

import java.io.Serializable

data class ShippingAddressModel(
    val address: String = "",
    val address_type: Int = 0,
    val email: String = "",
    val flat_number: String = "",
    val full_name: String = "",
    val id: Int = 0,
    val is_active: Boolean = false,
    val is_selected: Boolean = false,
    val landmark: String = "",
    val lat_long: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val mobile: String = "",
    val streat: String = "",
    var SelectedAddress: Boolean = false
) : Serializable