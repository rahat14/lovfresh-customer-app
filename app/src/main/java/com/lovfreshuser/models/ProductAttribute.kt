package com.lovfreshuser.models

import java.io.Serializable

data class ProductAttribute(
    val id: Int? = null,
    val price: String? = null,
    val product: Int? = null,
    val stock: String? = null,
    val total_sell_product: String? = null,
    val uom: Uom? = null
): Serializable {
    override fun toString(): String {
        return "${uom?.value} ${uom?.name} - $price"
    }
}