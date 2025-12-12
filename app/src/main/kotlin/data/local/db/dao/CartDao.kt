package com.application.data.local.db.dao

import androidx.room.*
import com.application.data.local.db.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItemEntity>>
    
    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartItemCount(): Flow<Int>
    
    @Query("SELECT * FROM cart_items WHERE itemId = :itemId")
    suspend fun getCartItemById(itemId: String): CartItemEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)
    
    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)
    
    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)
    
    @Query("DELETE FROM cart_items WHERE itemId = :itemId")
    suspend fun deleteCartItemById(itemId: String)
    
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

