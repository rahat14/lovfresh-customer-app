package com.lovfreshuser.models

data class Vendor_Product_Category(
    val id: Int? = null,
    val name: String? = null,
    val product_categories: List<ProductCategory>? = null

) {
    override fun toString(): String {
        return "Vendor_Product_Category(id=$id, name=$name, product_categories=$product_categories)"
    }
}