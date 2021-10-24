package com.lovfreshuser.models

import java.io.Serializable

data class User(
    val client_profile: ClientProfile? = null,
    val country_code: String? = null,
    val email: String? = null,
    val first_name: String? = null,
    val id: Int? = null,
    val last_name: String? = null,
    val national_number: String? = null,
    val phone_number: String? = null,
    val subscribe: Boolean? = null,
    val token: String? = null
):Serializable