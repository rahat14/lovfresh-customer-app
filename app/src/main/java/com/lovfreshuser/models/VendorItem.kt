package com.lovfreshuser.models

import java.io.Serializable

data class VendorItem(
    val address: Address? = null,
    val banner_images: List<BannerImage>? = null,
    val description: String? = null,
    val id: Int? = null,
    val title: String? = null
): Serializable