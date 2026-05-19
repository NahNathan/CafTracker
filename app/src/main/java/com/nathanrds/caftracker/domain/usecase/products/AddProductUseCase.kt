package com.nathanrds.caftracker.domain.usecase.products

import com.nathanrds.caftracker.domain.model.Product
import com.nathanrds.caftracker.domain.repository.ProductsRepository
import com.nathanrds.caftracker.domain.util.Result

class AddProductUseCase(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(product: Product): Result<Long> {
        if (product.name.isBlank()) {
            return Result.Error("Nome do produto não pode estar vazio")
        }
        if (product.caffeineMgPerUnit <= 0) {
            return Result.Error("A quantidade de cafeína por unidade deve ser maior que zero")
        }
        return try {
            val id = repository.insertProduct(product)
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error("Erro ao adicionar produto: ${e.message}")
        }
    }
}