package com.nathanrds.caftracker.domain.usecase.intakes

import com.nathanrds.caftracker.domain.model.Intake
import com.nathanrds.caftracker.domain.repository.IntakesRepository
import com.nathanrds.caftracker.domain.repository.ProductsRepository
import com.nathanrds.caftracker.domain.util.Result

class AddIntakeUseCase(
    private val intakesRepository: IntakesRepository,
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(intake: Intake): Result<Long> {
        if (intake.amount <= 0) {
            return Result.Error("A quantidade deve ser maior que zero")
        }
        
        val product = productsRepository.getProductById(intake.productId)
        if (product == null) {
            return Result.Error("Produto não encontrado")
        }
        
        return try {
            val id = intakesRepository.insertIntake(
                intake = intake,
                caffeineMgPerUnitSnapshot = product.caffeineMgPerUnit
            )
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error("Erro ao adicionar consumo: ${e.message}")
        }
    }
}