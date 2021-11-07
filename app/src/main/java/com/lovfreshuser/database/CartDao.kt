package com.lovfreshuser.database

import androidx.room.*
import com.lovfreshuser.database.models.CartLocalDbModel

@Dao
interface CartDao {


    @Query("SELECT * FROM order_items")
    fun getAll(): MutableList<CartLocalDbModel>

    @Query("SELECT * FROM order_items WHERE item_id == :id LIMIT 1  ")
    suspend fun findByFoodId(id: String): CartLocalDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(list: List<CartLocalDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartLocalDbModel)


    @Delete
    fun delete(item: CartLocalDbModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(vararg item: CartLocalDbModel)






}

interface ItemSelectInterface {
    fun itemSelect(id: String?, title: String?)
}