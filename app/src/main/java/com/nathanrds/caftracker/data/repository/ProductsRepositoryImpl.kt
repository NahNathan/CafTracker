package com.nathanrds.caftracker.data.repository

import com.nathanrds.caftracker.data.mapper.ProductMapper.toDomain
import com.nathanrds.caftracker.data.mapper.ProductMapper.toEntity
import com.nathanrds.caftracker.data.room.dao.ProductDao
import com.nathanrds.caftracker.domain.model.Product
import com.nathanrds.caftracker.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductsRepositoryImpl(
    private val productDao: ProductDao
) : ProductsRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)?.toDomain()
    }

    override suspend fun insertProduct(product: Product): Long {
        return productDao.insert(product.toEntity())
    }

    override suspend fun updateProduct(product: Product) {
        val existing = productDao.getProductById(product.id)
        val entity = product.toEntity(
            createdAtMillis = existing?.createdAtMillis ?: System.currentTimeMillis(),
            updatedAtMillis = System.currentTimeMillis()
        )
        productDao.update(entity)
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.delete(product.toEntity())
    }
}