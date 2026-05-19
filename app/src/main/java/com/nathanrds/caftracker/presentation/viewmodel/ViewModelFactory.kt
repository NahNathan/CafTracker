package com.nathanrds.caftracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nathanrds.caftracker.di.AppContainer

class ViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    container.getTodayTotalUseCase,
                    container.getTodayIntakesUseCase,
                    container.getLast7DaysSummaryUseCase,
                    container.productsRepository
                ) as T
            }
            modelClass.isAssignableFrom(ProductsViewModel::class.java) -> {
                ProductsViewModel(
                    container.getProductsUseCase,
                    container.deleteProductUseCase
                ) as T
            }
            modelClass.isAssignableFrom(AddIntakeViewModel::class.java) -> {
                AddIntakeViewModel(
                    container.getProductsUseCase,
                    container.addIntakeUseCase
                ) as T
            }
            modelClass.isAssignableFrom(AddEditProductViewModel::class.java) -> {
                AddEditProductViewModel(
                    container.productsRepository,
                    container.addProductUseCase,
                    container.updateProductUseCase
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

// Factory específica para AddEditProductViewModel que precisa do productId
class AddEditProductViewModelFactory(
    private val container: AppContainer,
    private val productId: Long?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditProductViewModel(
                container.productsRepository,
                container.addProductUseCase,
                container.updateProductUseCase,
                productId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
