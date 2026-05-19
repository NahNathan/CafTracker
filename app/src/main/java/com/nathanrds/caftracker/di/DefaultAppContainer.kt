package com.nathanrds.caftracker.di

import android.content.Context
import androidx.room.Room
import com.nathanrds.caftracker.data.repository.IntakesRepositoryImpl
import com.nathanrds.caftracker.data.repository.ProductsRepositoryImpl
import com.nathanrds.caftracker.data.room.db.CafTrackerDatabase
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

class DefaultAppContainer(context: Context) : AppContainer {
    private val database: CafTrackerDatabase = Room.databaseBuilder(
        context.applicationContext,
        CafTrackerDatabase::class.java,
        "caftracker_database"
    )
        .fallbackToDestructiveMigration(true) // Permite recriar o banco se houver problemas de migração
        .build()

    private val productDao = database.productDao()
    private val intakeDao = database.intakeDao()

    override val productsRepository: ProductsRepository = ProductsRepositoryImpl(productDao)
    override val intakesRepository: IntakesRepository = IntakesRepositoryImpl(intakeDao)

    override val getProductsUseCase: GetProductsUseCase = GetProductsUseCase(productsRepository)
    override val addProductUseCase: AddProductUseCase = AddProductUseCase(productsRepository)
    override val updateProductUseCase: UpdateProductUseCase = UpdateProductUseCase(productsRepository)
    override val deleteProductUseCase: DeleteProductUseCase = DeleteProductUseCase(productsRepository)

    override val getTodayIntakesUseCase: GetTodayIntakesUseCase = GetTodayIntakesUseCase(intakesRepository)
    override val getTodayTotalUseCase: GetTodayTotalUseCase = GetTodayTotalUseCase(intakesRepository)
    override val getLast7DaysSummaryUseCase: GetLast7DaysSummaryUseCase = GetLast7DaysSummaryUseCase(intakesRepository)
    override val addIntakeUseCase: AddIntakeUseCase = AddIntakeUseCase(intakesRepository, productsRepository)
}
