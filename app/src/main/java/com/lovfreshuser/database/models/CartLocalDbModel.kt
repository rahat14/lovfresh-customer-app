package com.lovfreshuser.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "order_items")
data class CartLocalDbModel(
    @PrimaryKey(autoGenerate = true)
    var id : Int= 0 ,
    var item_id: String = "",
    var itemName: String = "",
    var price: Double? = 0.0,
    var unit: Double = 0.0,
    var unitValue: Double = 0.0,
    var unitId: String = "",
    var quantity: Int = 0,
    var stockQuantity: Int =0,
    var image: String = "",
    var vendorId: String = "",
    val itemCount: Int = 0
)