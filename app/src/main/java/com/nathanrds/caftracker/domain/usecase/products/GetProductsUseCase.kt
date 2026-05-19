package com.nathanrds.caftracker.domain.usecase.products

import com.nathanrds.caftracker.domain.model.Product
import com.nathanrds.caftracker.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val repository: ProductsRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getAllProducts()
    }
}