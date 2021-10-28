package com.lovfreshuser.models

import java.io.Serializable

data class ProductDetailsModel(
    val created_at: String? = null,
    val end_date_time: String? = null,
    val id: Int? = null,
    val images: List<Image>? = null,
    val is_active: Boolean? = null,
    val is_hide: Boolean? = null,
    val long_description: String? = null,
    val price: String? = null,
    val product_attributes: List<ProductAttribute>? = null,
    val promotional: Boolean? = null,
    val promotional_price: String? = null,
    val quantity: String? = null,
    val short_description: String? = null,
    val start_date_time: String? = null,
    val title: String? = null,
    val type: String? = null,
    val updated_at: String? = null
): Serializable