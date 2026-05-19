package com.nathanrds.caftracker.domain.repository

import com.nathanrds.caftracker.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(id: Long): Product?
    suspend fun insertProduct(product: Product): Long
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
}