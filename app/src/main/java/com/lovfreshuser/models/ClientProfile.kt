package com.lovfreshuser.models

import java.io.Serializable

data class ClientProfile(
    val id: Int? = null,
    val image: String? = null,
    val is_updated: Boolean? = null,
    val user: Int? = null
): Serializable