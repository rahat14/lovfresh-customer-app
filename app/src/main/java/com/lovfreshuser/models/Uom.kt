package com.lovfreshuser.models

import java.io.Serializable

data class Uom(
    val formula: String? = null,
    val id: Int? = null,
    val name: String? = null,
    val value: Double? = null
): Serializable {
    override fun toString(): String {
        return "$value $name"
    }
}