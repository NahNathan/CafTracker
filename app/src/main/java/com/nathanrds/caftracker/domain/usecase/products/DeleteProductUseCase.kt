package com.nathanrds.caftracker.domain.usecase.products

import com.nathanrds.caftracker.domain.model.Product
import com.nathanrds.caftracker.domain.repository.ProductsRepository
import com.nathanrds.caftracker.domain.util.Result

class DeleteProductUseCase(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(product: Product): Result<Unit> {
        return try {
            repository.deleteProduct(product)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Erro ao deletar produto: ${e.message}")
        }
    }
}