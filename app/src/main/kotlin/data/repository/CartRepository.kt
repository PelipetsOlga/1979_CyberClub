package com.application.data.repository

import com.application.data.local.db.dao.CartDao
import com.application.data.local.db.entity.CartItemEntity
import com.application.domain.model.Item
import com.application.domain.model.ItemCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    fun getAllCartItems(): Flow<List<CartItemEntity>> = cartDao.getAllCartItems()
    
    fun getCartItemCount(): Flow<Int> = cartDao.getCartItemCount()
    
    suspend fun getCartItemById(itemId: String): CartItemEntity? = cartDao.getCartItemById(itemId)
    
    suspend fun addItemToCart(item: Item, quantity: Int = 1) {
        val existingItem = cartDao.getCartItemById(item.id)
        if (existingItem != null) {
            cartDao.updateCartItem(
                existingItem.copy(quantity = existingItem.quantity + quantity)
            )
        } else {
            cartDao.insertCartItem(
                CartItemEntity(
                    itemId = item.id,
                    title = item.title,
                    description = item.description,
                    price = item.price,
                    category = item.category.name,
                    iconResId = item.iconResId,
                    quantity = quantity
                )
            )
        }
    }
    
    suspend fun updateItemQuantity(itemId: String, quantity: Int) {
        val item = cartDao.getCartItemById(itemId)
        if (item != null) {
            if (quantity > 0) {
                cartDao.updateCartItem(item.copy(quantity = quantity))
            } else {
                cartDao.deleteCartItemById(itemId)
            }
        }
    }
    
    suspend fun removeItemFromCart(itemId: String) {
        cartDao.deleteCartItemById(itemId)
    }
    
    suspend fun clearCart() {
        cartDao.clearCart()
    }
    
    // Convert Entity to Item
    fun toItem(entity: CartItemEntity): Item {
        return Item(
            id = entity.itemId,
            title = entity.title,
            description = entity.description,
            price = entity.price,
            category = ItemCategory.valueOf(entity.category),
            iconResId = entity.iconResId
        )
    }
}

