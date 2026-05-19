package com.nathanrds.caftracker.di

import com.nathanrds.caftracker.domain.repository.IntakesRepository
import com.nathanrds.caftracker.domain.repository.ProductsRepository
import com.nathanrds.caftracker.domain.usecase.intakes.AddIntakeUseCase
import com.nathanrds.caftracker.domain.usecase.intakes.GetLast7DaysSummaryUseCase
import com.nathanrds.caftracker.domain.usecase.intakes.GetTodayIntakesUseCase
import com.nathanrds.caftracker.domain.usecase.intakes.GetTodayTotalUseCase
import com.nathanrds.caftracker.domain.usecase.products.AddProductUseCase
import com.nathanrds.caftracker.domain.usecase.products.DeleteProductUseCase
import com.nathanrds.caftracker.domain.usecase.products.GetProductsUseCase
import com.nathanrds.caftracker.domain.usecase.products.UpdateProductUseCase

interface AppContainer {
    val productsRepository: ProductsRepository
    val intakesRepository: IntakesRepository
    val getProductsUseCase: GetProductsUseCase
    val addProductUseCase: AddProductUseCase
    val updateProductUseCase: UpdateProductUseCase
    val deleteProductUseCase: DeleteProductUseCase
    val getTodayIntakesUseCase: GetTodayIntakesUseCase
    val getTodayTotalUseCase: GetTodayTotalUseCase
    val getLast7DaysSummaryUseCase: GetLast7DaysSummaryUseCase
    val addIntakeUseCase: AddIntakeUseCase
}