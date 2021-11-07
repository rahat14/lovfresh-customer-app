package com.lovfreshuser.models

import java.io.Serializable

data class Address(
    val address: String? = null,
    val address_type: Int? = null,
    val flat_number: Any? = null,
    val full_name: String? = null,
    val id: Int? = null,
    val is_active: Boolean? = null,
    val is_selected: Boolean? = null,
    val landmark: Any? = null,
    val lat_long: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    val mobile: Any? = null,
    val streat: Any? = null
):Serializable